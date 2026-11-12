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
package org.rumbledb.spark;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoopbackHostnameTest {
    @TempDir
    Path directory;

    @Test
    public void loopbackHostnameKeepsDriverReachableAndRunsJob() throws Exception {
        Path hosts = this.directory.resolve("hosts");
        Files.writeString(
            hosts,
            "127.0.0.1 localhost " + InetAddress.getLocalHost().getHostName() + "\n"
        );
        Path output = this.directory.resolve("spark.log");
        // Address selection is cached by both the JVM and Spark. Isolate the
        // resolver before either initializes, without editing the machine's hosts file.
        List<String> command = new ArrayList<>();
        command.add(Path.of(System.getProperty("java.home"), "bin", "java").toString());
        ManagementFactory.getRuntimeMXBean().getInputArguments().stream()
            .filter(argument -> argument.startsWith("--add-opens="))
            .forEach(command::add);
        command.add("-XX:ActiveProcessorCount=2");
        command.add("-Djdk.net.hosts.file=" + hosts.toAbsolutePath());
        command.add("-cp");
        command.add(System.getProperty("surefire.test.class.path", System.getProperty("java.class.path")));
        command.add(LoopbackHostnameTest.class.getName());
        ProcessBuilder builder = new ProcessBuilder(command)
            .redirectErrorStream(true)
            .redirectOutput(output.toFile());
        builder.environment().remove("SPARK_LOCAL_IP");
        builder.environment().remove("SPARK_LOCAL_HOSTNAME");
        Process process = builder.start();
        try {
            assertTrue(process.waitFor(60, TimeUnit.SECONDS), "Spark subprocess timed out:\n" + Files.readString(output));
            assertEquals(0, process.exitValue(), "Loopback hostname regression:\n" + Files.readString(output));
        } finally {
            process.descendants().forEach(ProcessHandle::destroyForcibly);
            process.destroyForcibly();
            process.waitFor(10, TimeUnit.SECONDS);
        }
    }

    public static void main(String[] arguments) {
        int status = 0;
        try {
            assertEquals("127.0.0.1", InetAddress.getLocalHost().getHostAddress(), "Invalid resolver fixture");
            System.setProperty("spark.master", "local[2]");
            System.setProperty("spark.ui.enabled", "false");
            // Model a driver accessible only via loopback. Do not supply driver.host:
            // RumbleDB must choose an address that clients can actually connect to.
            System.setProperty("spark.driver.bindAddress", "127.0.0.1");
            System.setProperty("spark.serializer", "org.apache.spark.serializer.JavaSerializer");
            SparkSession spark = SparkSessionManager.getInstance().getOrCreateSession();
            String host = spark.sparkContext().getConf().get("spark.driver.host");
            int port = spark.sparkContext().getConf().getInt("spark.driver.port", -1);
            System.out.println("Driver advertises " + host + ":" + port + " while bound to 127.0.0.1");
            try (Socket connection = new Socket()) {
                connection.connect(new InetSocketAddress(host, port), 5000);
            }
            assertEquals(1000L, spark.range(1000).repartition(2).count());
        } catch (Throwable failure) {
            failure.printStackTrace();
            status = 1;
        }
        // A failed Spark startup must not leave its service threads hanging in CI.
        System.exit(status);
    }
}
