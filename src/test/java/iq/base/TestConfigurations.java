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

package iq.base;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.items.ItemFactory;

import java.util.List;

public final class TestConfigurations {

    private TestConfigurations() {
    }

    public static RumbleRuntimeConfiguration defaultConfiguration() {
        return new RumbleRuntimeConfiguration(
                new String[] {
                    "--variable:externalUnparsedString",
                    "unparsed string",
                    "--materialization-cap",
                    "200" }
        ).setExternalVariableValue(
            Name.createVariableInNoNamespace("externalStringItem"),
            List.of(ItemFactory.getInstance().createStringItem("this is a string"))
        )
            .setExternalVariableValue(
                Name.createVariableInNoNamespace("externalIntegerItems"),
                List.of(
                    ItemFactory.getInstance().createIntItem(1),
                    ItemFactory.getInstance().createIntItem(2),
                    ItemFactory.getInstance().createIntItem(3),
                    ItemFactory.getInstance().createIntItem(4),
                    ItemFactory.getInstance().createIntItem(5)
                )
            );
    }
}
