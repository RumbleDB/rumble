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
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.exceptions;

import java.io.Serializable;

/**
 * 
 * Metadata for error reporting (line and column number)
 * 
 * @author Stefan Irimescu, Ghislain Fourny
 *
 */
public class ExceptionMetadata implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int _tokenLineNumber;
    private final int _tokenColumnNumber;

    /**
     * Builds a new metadata object
     * 
     * @param line the line number at which the error occurred.
     * @param column the column number at which the error occurred.
     */
    public ExceptionMetadata(int line, int column) {
        this._tokenLineNumber = line;
        this._tokenColumnNumber = column;

    }

    /**
     * Returns the line number.
     * 
     * @return the line number.
     */
    public int getTokenLineNumber() {
        return this._tokenLineNumber;
    }

    /**
     * Returns the column number.
     * 
     * @return the column number.
     */
    public int getTokenColumnNumber() {
        return this._tokenColumnNumber;
    }
}
