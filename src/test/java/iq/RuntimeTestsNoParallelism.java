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

package iq;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.items.ItemFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

@RunWith(Parameterized.class)
public class RuntimeTestsNoParallelism extends RuntimeTests {

    public RuntimeTestsNoParallelism(File testFile) {
        super(testFile);
    }

    public RumbleRuntimeConfiguration getConfiguration() {
        return new RumbleRuntimeConfiguration(
                new String[] {
                    "--print-iterator-tree",
                    "yes",
                    "--variable:externalUnparsedString",
                    "unparsed string",
                    "--parallel-execution",
                    "no", 
                    "--lax-json-null-validation",
                    "no" }
        ).setExternalVariableValue(
            Name.createVariableInNoNamespace("externalStringItem"),
            Collections.singletonList(ItemFactory.getInstance().createStringItem("this is a string"))
        )
            .setExternalVariableValue(
                Name.createVariableInNoNamespace("externalIntegerItems"),
                Arrays.asList(
                    new Item[] {
                        ItemFactory.getInstance().createIntItem(1),
                        ItemFactory.getInstance().createIntItem(2),
                        ItemFactory.getInstance().createIntItem(3),
                        ItemFactory.getInstance().createIntItem(4),
                        ItemFactory.getInstance().createIntItem(5),
                    }
                )
            );
    }
}
