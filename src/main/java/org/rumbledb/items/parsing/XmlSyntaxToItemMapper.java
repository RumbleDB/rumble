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
package org.rumbledb.items.parsing;

import java.io.Serial;
import java.io.StringReader;
import java.net.URI;
import java.util.Iterator;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import scala.Tuple2;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.xml.DocumentItem;

public class XmlSyntaxToItemMapper implements FlatMapFunction<Iterator<Tuple2<String, String>>, Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ExceptionMetadata metadata;

    private final boolean optimizeParentPointers;

    public XmlSyntaxToItemMapper(ExceptionMetadata metadata, boolean optimizeParentPointers) {
        this.metadata = metadata;
        this.optimizeParentPointers = optimizeParentPointers;
    }

    @Override
    public Iterator<Item> call(Iterator<Tuple2<String, String>> stringIterator) throws Exception {
        return new Iterator<Item>() {
            @Override
            public boolean hasNext() {
                return stringIterator.hasNext();
            }

            @Override
            public Item next() {
                Tuple2<String, String> tuple = stringIterator.next();
                String path = tuple._1;
                String content = tuple._2;
                Document xmlDocument = XmlParsingUtils.parseResource(
                        new InputSource(new StringReader(content)),
                        path,
                        "XML document \"" + path + "\" for jn:xml-files()",
                        XmlSyntaxToItemMapper.this.metadata);
                DocumentItem documentItem = ItemParser.getDocumentItemFromXML(
                        xmlDocument, path, XmlSyntaxToItemMapper.this.optimizeParentPointers);
                try {
                    URI docUri = URI.create(path);
                    if (docUri.isAbsolute()) {
                        documentItem.setConstructionBaseUri(docUri);
                    }
                } catch (IllegalArgumentException ignored) {
                }
                return documentItem;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
