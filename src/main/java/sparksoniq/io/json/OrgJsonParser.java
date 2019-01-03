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
 package sparksoniq.io.json;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrgJsonParser implements Serializable {

    public OrgJsonParser(){}

    protected List<JSONObject> parseJsonLinesFromIterator(Iterator<String> stringIterator){
        List<JSONObject> objects = new ArrayList<>();
        while(stringIterator.hasNext())
            objects.add(new JSONObject(stringIterator.next()));
        return objects;
    }

    protected List<JSONObject> parseJsonLines(List<String> jsonLines) {
        List<JSONObject> objects = new ArrayList<>();
        jsonLines.forEach(line -> objects.add(new JSONObject(line)));
        return objects;
    }

}
