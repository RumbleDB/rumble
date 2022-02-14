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

package org.rumbledb.shell;

import javassist.CannotCompileException;
import org.apache.commons.io.IOUtils;
import org.apache.spark.SparkException;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultHighlighter;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.rumbledb.api.Item;
import org.rumbledb.cli.JsoniqQueryExecutor;
import org.rumbledb.cli.Main;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.serialization.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RumbleJLineShell {
    private static final String EXIT_COMMAND = "exit";
    private static final String PROMPT = ANSIColor.CYAN + "RumbleDB$ " + ANSIColor.RESET;
    private static final String MID_QUERY_PROMPT = ">>> ";
    private final boolean printTime;
    private final RumbleRuntimeConfiguration configuration;
    private LineReader lineReader;
    private JsoniqQueryExecutor jsoniqQueryExecutor;
    private boolean queryStarted;
    private String previousLine = "";
    private String currentLine = "";
    private String currentQueryContent = "";
    private String welcomeMessage;

    public RumbleJLineShell(RumbleRuntimeConfiguration configuration) throws IOException {
        this.configuration = configuration;
        initialize();
        this.printTime = true;
    }

    public void launch() {
        this.output(getInitializationMessage());
        while (!exitCalled()) {
            try {
                this.currentLine = this.lineReader.readLine(getPrompt());
                if (!isConfig()) {

                    if (!this.currentLine.isEmpty()) {
                        this.currentQueryContent += "\n" + this.currentLine;
                        this.queryStarted = true;
                    }

                    if (isInQuery() && isQueryEnd()) {
                        processQuery();
                    }
                }
                this.previousLine = this.currentLine;
            } catch (Exception ex) {
                handleException(ex, this.configuration.getShowErrorInfo());
            }
        }
    }

    private void processQuery() throws IOException {
        String query = this.currentQueryContent.trim();
        long startTime = System.currentTimeMillis();
        List<Item> results = new ArrayList<>();
        try {
            long count = this.jsoniqQueryExecutor.runInteractive(query, results);
            Serializer serializer = this.configuration.getSerializer();
            String result = String.join(
                "\n",
                results.stream()
                    .map(x -> serializer.serialize(x))
                    .collect(Collectors.toList())
            );
            String shell = this.configuration.getShellFilter();
            if (shell != null) {
                Process process = Runtime.getRuntime().exec(shell);
                BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
                Writer stdin = new OutputStreamWriter(process.getOutputStream());
                stdin.write(result);
                stdin.flush();
                stdin.close();
                StringBuffer sb = new StringBuffer();
                String s = stdout.readLine();
                while (s != null) {
                    sb.append(s);
                    sb.append("\n");
                    s = stdout.readLine();
                }
                stdout.close();
                result = sb.toString();
                process.waitFor();
            }
            output(result);
            if (count != -1) {
                JsoniqQueryExecutor.issueMaterializationWarning(count);
            }
            long time = System.currentTimeMillis() - startTime;
            if (this.printTime) {
                output("The query took " + time + " milliseconds to execute.");
            }
        } catch (Exception ex) {
            handleException(ex, this.configuration.getShowErrorInfo());
        }
        this.queryStarted = false;
        this.currentQueryContent = "";
    }

    private void initialize() throws IOException {
        this.welcomeMessage = IOUtils.toString(Main.class.getResourceAsStream("/assets/banner.txt"), "UTF-8");
        this.welcomeMessage += "\n";
        this.welcomeMessage += IOUtils.toString(
            Main.class.getResourceAsStream("/assets/shell-instructions.txt"),
            "UTF-8"
        );
        Terminal terminal = TerminalBuilder.builder()
            .system(true)
            .build();
        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(null);
        this.lineReader = LineReaderBuilder.builder()
            .parser(parser)
            .terminal(terminal)
            // .completer(new MyCompleter())
            .highlighter(new DefaultHighlighter())
            // .parser(new JiqsJlineParser())
            .build();
        this.jsoniqQueryExecutor = new JsoniqQueryExecutor(this.configuration);
    }

    private void handleException(Throwable ex, boolean showErrorInfo) {
        if (ex != null) {
            if (ex instanceof EndOfFileException) {
                this.currentLine = RumbleJLineShell.EXIT_COMMAND;
            } else if (ex instanceof SparkException) {
                Throwable sparkExceptionCause = ex.getCause();
                if (sparkExceptionCause != null) {
                    handleException(sparkExceptionCause, showErrorInfo);
                } else {
                    if (showErrorInfo) {
                        ex.printStackTrace();
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
                System.err.println("⚠️  ️" + ex.getMessage());
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
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
            } else if (ex instanceof IllegalArgumentException) {
                System.err.println(
                    "⚠️  There was an IllegalArgumentException. Most of the time, this happens because you are not using Java 8. Spark only works with Java 8."
                );
                System.err.println(
                    "If you have several versions of java installed, you need to set your JAVA_HOME accordingly."
                );
                System.err.println("If you do not have Java 8 installed, we recommend installing AdoptOpenJDK 1.8.");
                System.err.println(
                    "For more debug info, please try again using --show-error-info yes in your command line."
                );
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
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
            } else if (ex instanceof ConnectException) {
                System.err.println("⚠️  There was a problem with the connection to the cluster.");
                System.err.println(
                    "For more debug info including the exact exception and a stacktrace, please try again using --show-error-info yes in your command line."
                );
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
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
            } else if (!(ex instanceof UserInterruptException)) {
                System.err.println("[ERROR] An error has occurred: " + ex.getMessage());
                System.err.println(
                    "We should investigate this 🙈. Please contact us or file an issue on GitHub with your query. "
                );
                System.err.println("Link: https://github.com/RumbleDB/rumble/issues");
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
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
            }
        }
    }

    public void output(String message) {
        System.err.println(ANSIColor.YELLOW + message + ANSIColor.RESET);
    }

    private String getPrompt() {
        if (isInQuery()) {
            return MID_QUERY_PROMPT;
        }
        return PROMPT;
    }

    private boolean isInQuery() {
        return !isConfig() && this.queryStarted;
    }

    private boolean isQueryEnd() {
        return this.currentLine != null
            && this.currentLine.equals("")
            && !this.currentQueryContent.isEmpty();
    }

    private boolean isConfig() {
        return false;
    }

    private boolean exitCalled() {
        return this.currentLine != null && this.currentLine.trim().toLowerCase().equals(RumbleJLineShell.EXIT_COMMAND);
    }

    private String getInitializationMessage() {
        return this.welcomeMessage + "\n" + this.configuration.toString();
    }


}
