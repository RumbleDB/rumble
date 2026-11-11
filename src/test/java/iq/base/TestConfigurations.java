/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package iq.base;

import java.util.List;

import org.rumbledb.api.ExternalBindings;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.items.ItemFactory;

public final class TestConfigurations {

    private TestConfigurations() {}

    public static RumbleConfiguration.RumbleConfigurationBuilder defaultConfigurationBuilder() {
        return RumbleConfiguration.builder().configureRuntime(r -> r.materializationCap(200));
    }

    public static RumbleConfiguration defaultConfiguration() {
        return defaultConfigurationBuilder().build();
    }

    public static ExternalBindings defaultExternalBindings() {
        ExternalBindings externalBindings = ExternalBindings.empty();
        externalBindings.bindItem(
                "externalStringItem", ItemFactory.getInstance().createStringItem("this is a string"));
        externalBindings.bindItems(
                "externalIntegerItems",
                List.of(
                        ItemFactory.getInstance().createIntItem(1),
                        ItemFactory.getInstance().createIntItem(2),
                        ItemFactory.getInstance().createIntItem(3),
                        ItemFactory.getInstance().createIntItem(4),
                        ItemFactory.getInstance().createIntItem(5)));
        externalBindings.bindLiteral("externalUnparsedString", "unparsed string");
        return externalBindings;
    }
}
