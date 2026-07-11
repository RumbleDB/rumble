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

package org.rumbledb.cli;

/**
 * Centralizes user-facing command-line output.
 *
 * Debug logging should be done through the logging framework, not through this class.
 */
public final class ConsoleOutput {

    private ConsoleOutput() {
    }

    /**
     * Prints user-facing text to standard output.
     *
     * @param message the text to print.
     */
    public static void out(String message) {
        System.out.println(message);
    }

    /**
     * Prints a user-facing warning to standard error.
     *
     * @param message the warning text to print.
     */
    public static void warn(String message) {
        System.err.println(message);
    }

    /**
     * Prints a user-facing error to standard error.
     *
     * @param message the error text to print.
     */
    public static void error(String message) {
        System.err.println(message);
    }

    /**
     * Prints a stack trace to standard error when the user explicitly requested debug error details.
     *
     * @param throwable the throwable whose stack trace should be printed.
     */
    public static void stackTrace(Throwable throwable) {
        throwable.printStackTrace(System.err);
    }
}
