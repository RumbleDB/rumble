/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
package sparksoniq.io.shell;

import org.apache.commons.io.IOUtils;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultHighlighter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import sparksoniq.JsoniqQueryExecutor;
import sparksoniq.Main;
import sparksoniq.config.SparksoniqRuntimeConfiguration;
import sparksoniq.spark.SparkContextManager;
import sparksoniq.utils.FileUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class JiqsJLineShell {
    private static final String EXIT_COMMAND = "exit";
    private static final String PROMPT = "jiqs$ ";
    private static final String MID_QUERY_PROMPT = ">>> ";
    private static final String ERROR_MESSAGE_PROMPT = "[ERROR] ";

    public JiqsJLineShell(SparksoniqRuntimeConfiguration configuration) throws IOException {
        this._configuration = configuration;
        this._itemLimit = 100;
        initialize();
        this._printTime = true;
    }

    public JiqsJLineShell(SparksoniqRuntimeConfiguration configuration, int itemLimit) throws IOException {
        this._configuration = configuration;
        this._itemLimit = itemLimit;
        initialize();
        this._printTime = true;
    }

    public void launch() {
        this.output(getInitializationMessage());
        while (!exitCalled()) {
            try {
                currentLine = lineReader.readLine(getPrompt());
                if (!isConfig()) {

                    if (!currentLine.isEmpty()) {
                        currentQueryContent += "\n" + currentLine;
                        queryStarted = true;
                    }

                    if (isInQuery() && isQueryEnd()) {
                        processQuery();
                    }
                }
                previousLine = currentLine;
            } catch (Exception ex) {
                handleException(ex);
            }
        }
    }

    private void processQuery() throws IOException {
        Path file = FileUtils.writeToFileInCurrentDirectory(currentQueryContent.trim());
        long startTime = System.currentTimeMillis();
        try {
            String result = jsoniqQueryExecutor.runInteractive(file);
            output(result);
            long time = System.currentTimeMillis() - startTime;
            if (_printTime)
                output("[EXEC TIME]: " + time);
            removeQueryFile(file);
        } catch (Exception ex) {
            handleException(ex);
            removeQueryFile(file);
        }
        queryStarted = false;
        currentQueryContent = "";
    }

    private void removeQueryFile(Path file) {
        try {
            Files.delete(file);
        } catch (Exception ignored) {
        }
    }

    private void initialize() throws IOException {
        welcomeMessage = IOUtils.toString(Main.class.getResourceAsStream("/assets/banner.txt"), "UTF-8");
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();
        lineReader = LineReaderBuilder.builder()

                .terminal(terminal)
//                .completer(new MyCompleter())
                .highlighter(new DefaultHighlighter())
//                .parser(new JiqsJlineParser())
                .build();
        PrintWriter outputWriter = new PrintWriter(terminal.output());
        jsoniqQueryExecutor = new JsoniqQueryExecutor(false, _itemLimit);
    }

    private void handleException(Exception ex) {
        if (ex != null) {
            if (ex instanceof EndOfFileException) {
                this.currentLine = this.EXIT_COMMAND;
            } else if (!(ex instanceof UserInterruptException)) {
                output(ERROR_MESSAGE_PROMPT + ex.getMessage().split("\n")[0]);
            }
        }
    }

    public void output(String message) {
        System.out.println(message);
    }

    private String getPrompt() {
        if (isInQuery())
            return MID_QUERY_PROMPT;
        return PROMPT;
    }

    private boolean isInQuery() {
        return !isConfig() && queryStarted;
    }

    private boolean isQueryEnd() {
        return previousLine != null && currentLine != null &&
                previousLine.equals("") && currentLine.equals("")
                && !currentQueryContent.isEmpty();
    }

    private boolean isConfig() {
        return false;
    }

    private boolean exitCalled() {
        return currentLine != null && this.currentLine.trim().toLowerCase().equals(JiqsJLineShell.EXIT_COMMAND);
    }

    private String getInitializationMessage() {
        return welcomeMessage + "\n" + _configuration.toString();
    }

    private final boolean _printTime;
    private final SparksoniqRuntimeConfiguration _configuration;
    private int _itemLimit;
    private LineReader lineReader;
    private JsoniqQueryExecutor jsoniqQueryExecutor;
    private boolean queryStarted;
    private String previousLine = "";
    private String currentLine = "";
    private String currentQueryContent = "";
    private String welcomeMessage;


}
