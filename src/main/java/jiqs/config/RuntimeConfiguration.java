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
 package jiqs.config;

public class RuntimeConfiguration {
    private String _masterUrl = "yarn-client";
    private String _inputDataSource = null;
    private String _clusterURL = null;
    private int _numExecutors = 50;

    private RuntimeConfiguration(){

    }

    public RuntimeConfiguration(String inputDataSource){
        this();
        this._inputDataSource = inputDataSource;
    }

    public RuntimeConfiguration(String inputDataSource, String masterUrl, String numExecutors){
        this(inputDataSource);
        this._masterUrl = masterUrl;
        this._numExecutors = Integer.valueOf(numExecutors);
    }

    @Override public String toString(){
        String result = "";
        result += "Input Data Source: " + _inputDataSource + "\n" +
                "Spark Master Mode: " + _masterUrl + "\n" +
                "Spark Cluster URL: " + _clusterURL + "\n" +
                "Number of executors: " + _numExecutors + "\n";
        return result;
    }
}
