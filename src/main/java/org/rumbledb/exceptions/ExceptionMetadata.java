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
 * Metadata for error reporting (line and column number)
 *
 * @author Stefan Irimescu, Ghislain Fourny
 */
public class ExceptionMetadata implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String location;
    private final int tokenLineNumber;
    private final int tokenColumnNumber;
    private final String code;
    public static final ExceptionMetadata EMPTY_METADATA = new ExceptionMetadata("none", 1, 0, "");

    /**
     * Builds a new metadata object
     *
     * @param location the URI of the JSONiq module at which the exception occurred.
     * @param line the line number at which the error occurred.
     * @param column the column number at which the error occurred.
     */
    public ExceptionMetadata(String location, int line, int column, String code) {
        this.location = location;
        this.tokenLineNumber = line;
        this.tokenColumnNumber = column;
        this.code = code;
    }

    /**
     * Returns the line number.
     *
     * @return the line number.
     */
    public int getTokenLineNumber() {
        return this.tokenLineNumber;
    }

    /**
     * Returns the column number.
     *
     * @return the column number.
     */
    public int getTokenColumnNumber() {
        return this.tokenColumnNumber;
    }

    /**
     * Returns the location.
     * 
     * @return the location.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Returns the column number.
     *
     * @return the column number.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns the error location in context.
     *
     * @return the code with a pointer to the error location.
     */
    public String getLineInContext() {
        StringBuffer buffer = new StringBuffer();
        String[] lines = this.code.split("\n");
        if (lines.length < this.tokenLineNumber) {
            return "";
        } else {
            buffer.append(lines[this.tokenLineNumber - 1]);
        }
        buffer.append("\n");
        for (int i = 0; i < this.tokenColumnNumber; ++i) {
            buffer.append(" ");
        }
        buffer.append("^\n");
        return buffer.toString();
    }

    public String toString() {
        return this.location
            + ":"
            + "LINE:"
            + getTokenLineNumber()
            +
            ":COLUMN:"
            + getTokenColumnNumber()
            + ":";
    }
}
