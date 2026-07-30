/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.runtime.flwor.udfs;

import com.esotericsoftware.kryo.Kryo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.items.FunctionBodyIteratorFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.FunctionItemType;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.UnionItemType;
import org.rumbledb.types.XmlNodeItemType;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataFrameContextKryoTest {

    @Test
    public void allConcreteItemsCanBeInstantiatedByDataFrameContextKryo()
            throws IOException,
                URISyntaxException {
        Kryo kryo = new DataFrameContext().getKryo();
        List<Class<?>> itemClasses = getConcreteItemClasses();

        Assertions.assertFalse(itemClasses.isEmpty(), "No concrete Item implementations were found.");
        for (Class<?> itemClass : itemClasses) {
            Assertions.assertDoesNotThrow(
                () -> {
                    itemClass.getDeclaredConstructor();
                },
                () -> itemClass.getName() + " must declare a no-argument constructor for DataFrameContext Kryo."
            );
            Assertions.assertDoesNotThrow(
                () -> kryo.getInstantiatorStrategy().newInstantiatorOf(itemClass).newInstance(),
                () -> itemClass.getName() + " cannot be instantiated by DataFrameContext Kryo."
            );
        }
    }

    @Test
    public void registeredSupportClassesDeclareNoArgumentConstructors() {
        List<Class<?>> supportClasses = List.of(
            FunctionIdentifier.class,
            Name.class,
            SequenceType.class,
            RumbleRuntimeConfiguration.class,
            FunctionBodyIteratorFactory.class,
            JSoundDataFrame.class,
            XMLDocumentPosition.class,
            Collection.class,
            FieldDescriptor.class,
            FunctionItemType.class,
            FunctionSignature.class,
            UnionItemType.class,
            XmlNodeItemType.class
        );

        for (Class<?> supportClass : supportClasses) {
            Assertions.assertDoesNotThrow(
                () -> {
                    supportClass.getDeclaredConstructor();
                },
                () -> supportClass.getName() + " must declare a no-argument constructor for DataFrameContext Kryo."
            );
        }
    }

    private List<Class<?>> getConcreteItemClasses()
            throws IOException,
                URISyntaxException {
        String packagePath = "org/rumbledb/items";
        ClassLoader classLoader = Item.class.getClassLoader();
        URL packageUrl = classLoader.getResource(packagePath);
        Assertions.assertNotNull(packageUrl, "Could not locate compiled Item classes.");
        Assertions.assertEquals(
            "file",
            packageUrl.getProtocol(),
            "Item classes must be available as files during tests."
        );

        Path packageRoot = Path.of(packageUrl.toURI());
        List<Class<?>> itemClasses = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(packageRoot)) {
            paths.filter(path -> path.getFileName().toString().endsWith(".class"))
                .map(path -> loadClass(packageRoot, path, packagePath, classLoader))
                .filter(Item.class::isAssignableFrom)
                .filter(clazz -> !clazz.isInterface())
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .forEach(itemClasses::add);
        }
        return itemClasses;
    }

    private Class<?> loadClass(Path packageRoot, Path classFile, String packagePath, ClassLoader classLoader) {
        String relativeClassName = packageRoot.relativize(classFile).toString();
        String className = packagePath.replace('/', '.')
            + "."
            + relativeClassName.substring(0, relativeClassName.length() - ".class".length())
                .replace(classFile.getFileSystem().getSeparator(), ".");
        try {
            return Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load " + className, e);
        }
    }
}
