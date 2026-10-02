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
package org.rumbledb.serialization;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/** Writes independently serialized items with a newline after each record, using the requested encoding. */
public class SerializedItemOutputFormat extends FileOutputFormat<NullWritable, Text> {
    public static final String ENCODING = "rumbledb.output.encoding";

    @Override
    public RecordWriter<NullWritable, Text> getRecordWriter(TaskAttemptContext context) throws IOException {
        Path path = getDefaultWorkFile(context, "");
        Writer writer = new BufferedWriter(new OutputStreamWriter(
                path.getFileSystem(context.getConfiguration()).create(path, false),
                Charset.forName(context.getConfiguration().get(ENCODING, "UTF-8"))));
        return new RecordWriter<>() {
            @Override
            public void write(NullWritable key, Text value) throws IOException {
                writer.write(value.toString());
                writer.write('\n');
            }

            @Override
            public void close(TaskAttemptContext taskContext) throws IOException {
                writer.close();
            }
        };
    }
}
