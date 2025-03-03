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
 *
 * Authors: Marco Schöb
 *
 */

package org.rumbledb.items.parsing;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import scala.Tuple2;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

public class XmlSyntaxToItemMapper implements FlatMapFunction<Iterator<Tuple2<String, String>>, Item> {

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
                try {
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document xmlDocument = documentBuilder.parse(new InputSource(new StringReader(content)));
                    return ItemParser.getItemFromXML(xmlDocument, path, optimizeParentPointers);
                } catch (ParserConfigurationException e) {
                    throw new OurBadException("Document builder creation failed with: " + e);
                } catch (IOException e) {
                    throw new RuntimeException("IOException while reading XML document." + content + e);
                } catch (SAXException e) {
                    throw new RuntimeException("SAXException while reading XML document." + content + e);
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
