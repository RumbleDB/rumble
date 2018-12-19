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
 package sparksoniq.config;

import java.util.HashMap;

import sparksoniq.exceptions.CliException;

public class SparksoniqRuntimeConfiguration {

    public static final String ARGUMENT_PREFIX = "--";
    public static HashMap<String, String> processCommandLineArgs(String[] args) {
        HashMap<String, String> argumentMap = new HashMap<>();
        for(int i = 0; i< args.length; i+=2)
            if(args[i].startsWith(ARGUMENT_PREFIX))
                argumentMap.put(args[i].trim().replace(ARGUMENT_PREFIX, ""), args[i+1]);
            else
                throw new CliException(ARGUMENT_FORMAT_ERROR_MESSAGE);
        return argumentMap;
    }

    public SparksoniqRuntimeConfiguration(HashMap<String, String> arguments) {
        this._arguments = arguments;
    }

    public String getConfigurationArgument(String key){
        if(this._arguments.containsKey(key))
            return this._arguments.get(key);
        else
            return null;
    }

    @Override public String toString(){
        String result = "";
        result += "Item Display Limit: " + (_arguments.getOrDefault("result-size", "-")) + "\n" +
                "Output Path: " + (_arguments.getOrDefault("output-path", "-")) + "\n" +
                "Log Path: " + (_arguments.getOrDefault("log-path", "-")) + "\n" +
                "Query Path : " + (_arguments.getOrDefault("query-path", "-")) + "\n";
        return result;
    }

    private static final String ARGUMENT_FORMAT_ERROR_MESSAGE = "Invalid argument format. Required format: --property value";
    private HashMap<String, String> _arguments;
}
