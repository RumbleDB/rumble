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

import org.antlr.v4.runtime.Token;

/**
 * Metadata for error reporting (line and column number)
 *
 * @author Stefan Irimescu, Ghislain Fourny
 */
public class ExceptionMetadata implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String location;
    private final SourceRange range;
    private final String code;
    public static final ExceptionMetadata EMPTY_METADATA = new ExceptionMetadata(
            "none",
            SourceRange.point(1, 0),
            ""
    );

    /**
     * Builds a new metadata object
     *
     * @param location the URI of the JSONiq module at which the exception occurred.
     * @param startLine the starting line number at which the error occurred.
     * @param startColumn the starting column number at which the error occurred.
     * @param endLine the ending line number at which the error occurred.
     * @param endColumn the ending column number at which the error occurred.
     * @param code the query code around the error.
     */
    public ExceptionMetadata(
            String location,
            int startLine,
            int startColumn,
            int endLine,
            int endColumn,
            String code
    ) {
        this(
            location,
            new SourceRange(
                    new SourcePosition(startLine, startColumn),
                    new SourcePosition(endLine, endColumn)
            ),
            code
        );
    }

    public ExceptionMetadata(String location, SourceRange range, String code) {
        this.location = location;
        this.range = range;
        this.code = code;
    }

    public static ExceptionMetadata fromToken(String location, Token token, String code) {
        return fromTokens(location, token, token, code);
    }

    public static ExceptionMetadata fromPoint(String location, int line, int column, String code) {
        return new ExceptionMetadata(location, SourceRange.point(line, column), code);
    }

    public static ExceptionMetadata fromTokens(String location, Token start, Token end, String code) {
        if (end == null) {
            end = start;
        }
        String endText = end.getText();
        int endColumn = end.getCharPositionInLine() + (endText == null ? 0 : endText.length());
        return new ExceptionMetadata(
                location,
                start.getLine(),
                start.getCharPositionInLine(),
                end.getLine(),
                endColumn,
                code
        );
    }

    /**
     * Returns the location.
     * 
     * @return the location.
     */
    public String getLocation() {
        return this.location;
    }

    public SourceRange getRange() {
        return this.range;
    }

    public SourcePosition getStart() {
        return this.range.start();
    }

    public SourcePosition getEnd() {
        return this.range.end();
    }

    /**
     * Returns the the query code around the error.
     *
     * @return the the query code around the error.
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
        if (lines.length < this.range.start().line()) {
            return "";
        }
        String line = lines[this.range.start().line() - 1];
        buffer.append(line);
        buffer.append("\n");
        for (int i = 0; i < this.range.start().column(); ++i) {
            buffer.append(" ");
        }
        if (this.range.start().line() == this.range.end().line()) {
            int width = this.range.end().column() - this.range.start().column();
            if (width <= 0) {
                width = 1;
            }
            int maxWidth = Math.max(1, line.length() - this.range.start().column());
            width = Math.min(width, maxWidth);
            for (int i = 0; i < width; ++i) {
                buffer.append("^");
            }
        } else {
            buffer.append("^");
        }
        buffer.append("\n");
        return buffer.toString();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.location);
        buffer.append(":LINE:");
        buffer.append(this.range.start().line());
        buffer.append(":COLUMN:");
        buffer.append(this.range.start().column());
        buffer.append(":");
        buffer.append("ENDLINE:");
        buffer.append(this.range.end().line());
        buffer.append(":ENDCOLUMN:");
        buffer.append(this.range.end().column());
        buffer.append(":");
        return buffer.toString();
    }
}
