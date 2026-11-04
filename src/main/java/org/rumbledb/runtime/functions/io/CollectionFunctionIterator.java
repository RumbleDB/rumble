/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.functions.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serial;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.items.parsing.JSONParser;
import org.rumbledb.items.parsing.JSONParsingOptions;
import org.rumbledb.resources.ResourceCollection;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

public class CollectionFunctionIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {
    @Serial
    private static final long serialVersionUID = 1L;

    public CollectionFunctionIterator(List<ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    protected ResourceCollection collection(DynamicContext context) {
        Item argument = getChildren().isEmpty() ? null : getChild(0).materializeAtMostOne(context);
        URI uri = null;
        if (argument != null) {
            try {
                uri = URI.create(argument.getStringValue());
                if (!uri.isAbsolute()) {
                    uri = this.staticContext.getStaticURI().resolve(uri);
                }
            } catch (IllegalArgumentException exception) {
                throw new CannotRetrieveResourceException(
                        "Invalid collection URI: " + argument.getStringValue(), getMetadata());
            }
        }
        try {
            return context.getResourceResolver().collection(uri, getMetadata());
        } catch (CannotRetrieveResourceException exception) {
            if (uri == null
                    || uri.isOpaque()
                    || !"file".equalsIgnoreCase(uri.getScheme())
                    || uri.getRawAuthority() != null
                    || uri.getRawQuery() != null
                    || uri.getRawFragment() != null) {
                throw exception;
            }
            Path directory = Path.of(uri);
            if (!Files.isDirectory(directory)) {
                throw exception;
            }
            return loadDirectory(directory, context);
        }
    }

    private ResourceCollection loadDirectory(Path directory, DynamicContext context) {
        List<Path> files;
        try (Stream<Path> entries = Files.list(directory)) {
            files = entries.filter(Files::isRegularFile)
                    .filter(path -> {
                        String name = path.getFileName().toString();
                        return name.endsWith(".xml") || name.endsWith(".json") || name.endsWith(".jsonl");
                    })
                    .sorted()
                    .toList();
        } catch (IOException | UncheckedIOException exception) {
            CannotRetrieveResourceException error = new CannotRetrieveResourceException(
                    "Unable to list collection directory " + directory + ".", getMetadata());
            error.initCause(exception);
            throw error;
        }
        List<Item> items = new ArrayList<>();
        for (Path file : files) {
            if (file.getFileName().toString().endsWith(".xml")) {
                items.add(DocFunctionIterator.loadDocument(
                        file.toUri(), context, this.staticContext.getQueryLanguage(), getMetadata()));
            } else {
                loadJsonFile(file, context, items);
            }
        }
        return new ResourceCollection(items, files.stream().map(Path::toUri).toList());
    }

    private void loadJsonFile(Path file, DynamicContext context, List<Item> items) {
        boolean jsoniq10 = "jsoniq10".equals(this.staticContext.getQueryLanguage());
        JSONParsingOptions options =
                JSONParsingOptions.resolveOptions(null, jsoniq10, context, this.staticContext, getMetadata());
        String xmlVersion = getConfiguration().semantics().xmlVersion();
        URI uri = file.toUri();
        try {
            if (file.getFileName().toString().endsWith(".jsonl")) {
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    long lineNumber = 0;
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        Item item = JSONParser.parseResource(
                                line,
                                options,
                                xmlVersion,
                                jsoniq10,
                                uri,
                                "JSON Lines file " + uri + ", line " + lineNumber,
                                getMetadata());
                        if (item != null) {
                            items.add(item);
                        }
                    }
                }
            } else {
                Item item = JSONParser.parseResource(
                        Files.readString(file), options, xmlVersion, jsoniq10, uri, "JSON file " + uri, getMetadata());
                if (item != null) {
                    items.add(item);
                }
            }
        } catch (IOException exception) {
            CannotRetrieveResourceException error =
                    new CannotRetrieveResourceException("Unable to read collection member " + uri + ".", getMetadata());
            error.initCause(exception);
            throw error;
        }
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> collection(context).items().iterator(), getMetadata());
    }
}
