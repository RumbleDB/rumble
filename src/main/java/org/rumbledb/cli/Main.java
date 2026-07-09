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
package org.rumbledb.cli;

import java.io.IOException;
import java.net.ConnectException;


import org.apache.commons.io.IOUtils;
import org.apache.spark.SparkException;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.config.model.RumbleMode;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.shell.RumbleJLineShell;

public class Main {
    public static RumbleJLineShell terminal = null;

    public static void main(String[] args) throws IOException {
        String javaVersion = System.getProperty("java.version");
        if (
            !javaVersion.startsWith("17")
                && !javaVersion.startsWith("21")
        ) {
            ConsoleOutput.error(
                """
                        [Error] RumbleDB requires Java 17 or 21 (17 being the default Spark 4 version).
                        Your Java version: %s
                        You can download Java 17 or 21 from https://adoptium.net/
                        If you do have Java 17 or 21, but the wrong version appears above, then it means you need to set your JAVA_HOME environment variable properly to point to Java 17 or 21.\
                        """
                    .formatted(System.getProperty("java.version"))
            );
            System.exit(43);
        }

        CLIInvocation invocation = null;
        RumbleConfiguration configuration = null;
        try {
            invocation = CLIArgumentParser.parse(args);

            if (invocation == null) {
                System.exit(0);
            } else {
                configuration = invocation.configuration();
            }
        } catch (Exception e) {
            System.err.println("⚠️ CLI Error: " + e.getMessage());
            System.exit(42);
        }

        try {
            if (configuration.mode() == RumbleMode.REPL) {
                launchShell(invocation);
            } else if (configuration.input().query() != null || configuration.input().queryPath() != null) {
                runQueryExecutor(invocation);
            } else {
                ConsoleOutput.out(
                    """
                                %s
                                %s
                            """.formatted(
                        IOUtils.toString(Main.class.getResourceAsStream("/assets/banner.txt"), "UTF-8"),
                        IOUtils.toString(Main.class.getResourceAsStream("/assets/defaultscreen.txt"), "UTF-8")

                    )
                );
            }
            System.exit(0);
        } catch (Exception ex) {
            boolean showErrorInfo = false;
            if (configuration != null) {
                showErrorInfo = configuration.debug().showErrorInfo();
            }
            handleException(ex, showErrorInfo);
        } catch (OutOfMemoryError ex) {
            boolean showErrorInfo = false;
            if (configuration != null) {
                showErrorInfo = configuration.debug().showErrorInfo();
            }
            handleException(ex, showErrorInfo);
        }
    }

    private static void handleException(Throwable ex, boolean showErrorInfo) {
        if (ex != null) {
            if (ex instanceof SparkException) {
                Throwable sparkExceptionCause = ex.getCause();
                if (sparkExceptionCause != null) {
                    handleException(sparkExceptionCause, showErrorInfo);
                } else {
                    if (showErrorInfo) {
                        ConsoleOutput.stackTrace(ex);
                    }
                    handleException(
                        new OurBadException(
                                "There was a problem with Spark, but Spark did not provide any cause or stracktrace. The message from Spark is:  "
                                    + ex.getMessage()
                        ),
                        showErrorInfo
                    );
                }
            } else if (ex instanceof RumbleException && !(ex instanceof OurBadException)) {
                ConsoleOutput.error("⚠️ " + ex.getMessage());
                if (showErrorInfo) {
                    ConsoleOutput.stackTrace(ex);
                }
                System.exit(42);
            } else if (ex instanceof OutOfMemoryError) {
                ConsoleOutput.error(
                    """
                            ⚠️  Java went out of memory.
                            If running locally, try adding --driver-memory 10G (or any quantity you need) between spark-submit and the RumbleDB jar in the command line to see if it fixes the problem. If running on a cluster, --executor-memory is the way to go.\
                            """
                );
                if (showErrorInfo) {
                    ConsoleOutput.stackTrace(ex);
                }
                System.exit(46);
            } else if (ex instanceof ConnectException) {
                ConsoleOutput.error(
                    """
                            ⚠️  There was a problem with the connection to the cluster.
                            For more debug info including the exact exception and a stacktrace, please try again using --show-error-info yes in your command line.\
                            """
                );
                if (showErrorInfo) {
                    ConsoleOutput.stackTrace(ex);
                }
                System.exit(45);
            } else if (ex instanceof NullPointerException) {
                ConsoleOutput.error(
                    """
                            Oh my oh my, we are very embarrassed, because there was a null pointer exception. 🙈
                            We would like to investigate this and make sure to fix it in a subsequent release. We would be very grateful if you could contact us or file an issue on GitHub with your query.
                            Link: https://github.com/RumbleDB/rumble/issues
                            For more debug info (e.g., so you can communicate it to us), please try again using --show-error-info yes in your command line.\
                            """
                );
                if (showErrorInfo) {
                    ConsoleOutput.stackTrace(ex);
                }
                System.exit(-42);
            } else {
                ConsoleOutput.error(
                    """
                            We are very embarrassed, because an error has occured that we did not anticipate 🙈: %s
                            We would like to investigate this and make sure to fix it. We would be very grateful if you could contact us or file an issue on GitHub with your query.
                            Link: https://github.com/RumbleDB/rumble/issues
                            For more debug info (e.g., so you can communicate it to us), please try again using --show-error-info yes in your command line.\
                            """
                        .formatted(ex.getMessage())
                );
                if (showErrorInfo) {
                    ConsoleOutput.stackTrace(ex);
                }
                System.exit(-42);
            }
        }
    }

    private static void runQueryExecutor(CLIInvocation invocation) throws IOException {
        JsoniqQueryExecutor translator = new JsoniqQueryExecutor(
                invocation.configuration(),
                invocation.bindings()
        );
        translator.runQuery();
    }

    private static void launchShell(CLIInvocation invocation) throws IOException {
        terminal = new RumbleJLineShell(invocation.configuration(), invocation.bindings());
        terminal.launch();
    }

    public static void printMessageToLog(String message) {
        if (Main.terminal == null) {
            ConsoleOutput.out(message);
        } else {
            Main.terminal.output(message);
        }
    }

}
