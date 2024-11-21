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

package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.Serializable;
import java.util.Objects;

/**
 * The `XMLDocumentPosition` class represents the position of an item within an XML document.
 * It provides information about the document's path and the item's position within the document.
 * This class is used to ensure the uniqueness and ordering of items across XML documents.
 */
public class XMLDocumentPosition implements Comparable<XMLDocumentPosition>, Serializable, KryoSerializable {
    private String path;
    private int docPosition;

    public XMLDocumentPosition(String path, int docPosition) {
        this.path = path;
        this.docPosition = docPosition;
    }


    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(path);
        output.writeInt(docPosition);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.path = input.readString();
        this.docPosition = input.readInt();
    }

    public String getPath() {
        return path;
    }

    public int getDocPosition() {
        return docPosition;
    }

    @Override
    public int compareTo(XMLDocumentPosition o) {
        int pathResult = this.path.compareTo(o.getPath());
        if (pathResult == 0) {
            return this.docPosition - o.getDocPosition();
        }
        return pathResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        XMLDocumentPosition that = (XMLDocumentPosition) o;
        return getDocPosition() == that.getDocPosition() && Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath(), getDocPosition());
    }
}
