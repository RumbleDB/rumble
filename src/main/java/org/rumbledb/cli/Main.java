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
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.server.RumbleServer;
import org.rumbledb.shell.RumbleJLineShell;

import javassist.CannotCompileException;

public class Main {
    public static RumbleJLineShell terminal = null;

    public static void main(String[] args) throws IOException {
        String javaVersion = System.getProperty("java.version");
        if (!javaVersion.startsWith("1.8") && !javaVersion.startsWith("11.")) {
            System.err.println("[Error] RumbleDB requires Java 8 or Java 11.");
            System.err.println("Your Java version: " + System.getProperty("java.version"));
            System.err.println("You can download Java 8 or 11 from https://adoptium.net/");
            System.err.println(
                "If you do have Java 8 or 11, but the wrong version appears above, then it means you need to set your JAVA_HOME environment variable properly to point to Java 8 or 11."
            );
            System.exit(43);
        }
        RumbleRuntimeConfiguration sparksoniqConf = null;
        // Parse arguments
        try {
            sparksoniqConf = new RumbleRuntimeConfiguration(args);

            if (sparksoniqConf.isShell()) {
                launchShell(sparksoniqConf);
            } else if (sparksoniqConf.isServer()) {
                launchServer(sparksoniqConf);
            } else if (sparksoniqConf.getQuery() != null || sparksoniqConf.getQueryPath() != null) {
                runQueryExecutor(sparksoniqConf);
            } else {
                System.out.println(IOUtils.toString(Main.class.getResourceAsStream("/assets/banner.txt"), "UTF-8"));
                System.out.println();
                System.out.println(
                    IOUtils.toString(Main.class.getResourceAsStream("/assets/defaultscreen.txt"), "UTF-8")
                );
            }
            System.exit(0);
        } catch (Exception ex) {
            boolean showErrorInfo = false;
            if (sparksoniqConf != null) {
                showErrorInfo = sparksoniqConf.getShowErrorInfo();
            }
            handleException(ex, showErrorInfo);
        } catch (OutOfMemoryError ex) {
            boolean showErrorInfo = false;
            if (sparksoniqConf != null) {
                showErrorInfo = sparksoniqConf.getShowErrorInfo();
            }
            handleException(ex, showErrorInfo);
        }
    }

    private static void handleException(Throwable ex, boolean showErrorInfo) {
        ex = unboxException(ex);
        if (ex != null) {
            if (ex instanceof RumbleException && !(ex instanceof OurBadException)) {
                System.err.println("⚠️  ️" + ex.getMessage());
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
                System.exit(42);
            } else if (ex instanceof NumberFormatException) {
                handleException(
                    new UnexpectedTypeException(
                            "A cast failed. However, since this happened in a native Spark SQL execution, we cannot show you where. You can get more information on this type error by retrying with --native-execution no.",
                            ExceptionMetadata.EMPTY_METADATA
                    ),
                    showErrorInfo
                );
            } else if (ex instanceof OutOfMemoryError) {
                System.err.println(
                    "⚠️  Java went out of memory."
                );
                System.err.println(
                    "If running locally, try adding --driver-memory 10G (or any quantity you need) between spark-submit and the RumbleDB jar in the command line to see if it fixes the problem. If running on a cluster, --executor-memory is the way to go."
                );
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
                System.exit(46);
            } else if (ex instanceof CannotCompileException) {
                System.err.println("⚠️  There was a CannotCompileException.");
                System.err.println(
                    "There is a known issue with this on Docker and on certain versions of OpenJDK due to the JSONiter library."
                );
                System.err.println(
                    "We have a workaround: please try again using --deactivate-jsoniter-streaming yes on your command line. json-doc() will, however, not be available."
                );
                System.err.println(
                    "For more debug info, please try again using --show-error-info yes in your command line."
                );
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
                System.exit(44);
            } else if (ex instanceof ConnectException) {
                System.err.println("⚠️  There was a problem with the connection to the cluster.");
                System.err.println(
                    "For more debug info including the exact exception and a stacktrace, please try again using --show-error-info yes in your command line."
                );
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
                System.exit(45);
            } else if (ex instanceof NullPointerException) {
                System.err.println(
                    "Oh my oh my, we are very embarrassed, because there was a null pointer exception. 🙈"
                );
                System.err.println(
                    "We would like to investigate this and make sure to fix it in a subsequent release. We would be very grateful if you could contact us or file an issue on GitHub with your query."
                );
                System.err.println("Link: https://github.com/RumbleDB/rumble/issues");
                System.err.println(
                    "For more debug info (e.g., so you can communicate it to us), please try again using --show-error-info yes in your command line."
                );
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
                System.exit(-42);
            } else {
                System.err.println(
                    "We are very embarrassed, because an error has occured that we did not anticipate 🙈: "
                        + ex.getMessage()
                );
                System.err.println(
                    "We would like to investigate this and make sure to fix it. We would be very grateful if you could contact us or file an issue on GitHub with your query."
                );
                System.err.println("Link: https://github.com/RumbleDB/rumble/issues");
                System.err.println(
                    "For more debug info (e.g., so you can communicate it to us), please try again using --show-error-info yes in your command line."
                );
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
                System.exit(-42);
            }
        }
    }

    public static Throwable unboxException(Throwable ex) {
        if (ex != null) {
            if (ex instanceof SparkException) {
                Throwable sparkExceptionCause = ex.getCause();
                if (sparkExceptionCause != null) {
                    return sparkExceptionCause;
                }
                return new OurBadException(
                        "There was a problem with Spark, but Spark did not provide any cause or stracktrace. The message from Spark is:  "
                            + ex.getMessage()
                );
            }
            if (ex instanceof NumberFormatException) {
                return new UnexpectedTypeException(
                        "A cast failed. However, since this happened in a native Spark SQL execution, we cannot show you where. You can get more information on this type error by retrying with --native-execution no.",
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
            return ex;
        }
        return new OurBadException("A null exception was returned.");
    }

    private static void runQueryExecutor(RumbleRuntimeConfiguration sparksoniqConf) throws IOException {
        JsoniqQueryExecutor translator = new JsoniqQueryExecutor(sparksoniqConf);
        translator.runQuery();
    }

    private static void launchShell(RumbleRuntimeConfiguration sparksoniqConf) throws IOException {
        terminal = new RumbleJLineShell(sparksoniqConf);
        terminal.launch();
    }

    private static void launchServer(RumbleRuntimeConfiguration sparksoniqConf) throws IOException {
        RumbleServer server = new RumbleServer(sparksoniqConf);
        server.start();
    }

    public static void printMessageToLog(String message) {
        if (Main.terminal == null) {
            System.out.println(message);
        } else {
            Main.terminal.output(message);
        }
    }

}
