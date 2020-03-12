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

package sparksoniq.io.validation;


import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class OutputValidator {
    private static String ZORBA_FILE_PATH = // "/home/stefan/Work/ETH/Thesis/benchmarks/validation/zorba-results/conf1";
        "/home/stefan/Desktop/media_sdb1/Scoala/ETH/Big_Data/validation/zorba-results/where1Zorba";
    // private static String SPARK_OUTPUT_DIR =
    // "/home/stefan/Work/ETH/Thesis/benchmarks/validation/output-where/output-where-confusion1/output/";
    private static String SPARK_OUTPUT_DIR = "/home/stefan/Desktop/media_sdb1/Scoala/ETH/Big_Data/validation/output/";

    public static void main(String[] args) throws IOException {
        File sparkDir = new File(SPARK_OUTPUT_DIR);
        File merge = new File(SPARK_OUTPUT_DIR + "MERGE");
        if (!merge.exists()) {
            List<File> files = Arrays.asList(sparkDir.listFiles());
            files.sort(Comparator.comparing(File::getAbsolutePath));
            for (File f : files) {
                String fileString = FileUtils.readFileToString(f, StandardCharsets.UTF_8);
                FileUtils.write(merge, fileString, StandardCharsets.UTF_8, true);
            }
        }

        BufferedReader zorbaReader = null, outputReader = null;
        String zorbaLine, outputLine;
        boolean success = true;
        try {
            zorbaReader = new BufferedReader(new FileReader(ZORBA_FILE_PATH));
            outputReader = new BufferedReader(new FileReader(merge));
            zorbaLine = zorbaReader.readLine();
            while ((zorbaLine = zorbaReader.readLine()) != null) {
                zorbaLine = zorbaLine.trim()
                    .replace(" ", "")
                    .replace("\"", "");
                outputLine = outputReader.readLine()
                    .trim()
                    .replace(" ", "")
                    .replace("\"", "");
                if (!zorbaLine.equals(outputLine)) {
                    System.out.println("Failed");
                    System.out.println(zorbaLine);
                    System.out.println(outputLine);
                    success = false;
                    break;
                }

            }

            if (success) {
                System.out.println("OK");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zorbaReader != null) {
                    zorbaReader.close();
                }
                if (outputReader != null) {
                    outputReader.close();
                }
                merge.delete();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
