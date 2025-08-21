package iq;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.spark.SparkConf;
import org.junit.Assert;
import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.cli.JsoniqQueryExecutor;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import scala.util.Properties;
import scala.Function0;
import sparksoniq.spark.SparkSessionManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.function.Consumer;

public class UpdatesForRumbleBenchmark {

    private static final String APP_NAME = "Rumble application";
    public static final String javaVersion =
        System.getProperty("java.version");
    public static final String scalaVersion =
        Properties.scalaPropOrElse("version.number", new Function0<String>() {
            @Override
            public String apply() {
                return "unknown";
            }
        });

    public List<FileTuple> benchmarkFiles;

    protected static final RumbleRuntimeConfiguration configuration = new RumbleRuntimeConfiguration(
            new String[] {
                "--variable:externalUnparsedString",
                "unparsed string",
                "--escape-backticks",
                "yes",
                "--dates-with-timezone",
                "yes",
                "--print-iterator-tree",
                "yes",
                "--apply-updates",
                "yes",
                "--show-error-info",
                "yes",
                "--materialization-cap",
                "900000"
            }
    );

    protected static final RumbleRuntimeConfiguration createDeltaConfiguration = new RumbleRuntimeConfiguration(
            new String[] {
                "--print-iterator-tree",
                "yes",
                "--output-format",
                "delta",
                "--show-error-info",
                "yes",
                "--apply-updates",
                "yes",
                "--materialization-cap",
                "900000"
            }
    );

    public UpdatesForRumbleBenchmark() {
        this.benchmarkFiles = new ArrayList<>();
        List<Integer> powersOf2 = new ArrayList<>();
        powersOf2.add(2);
        powersOf2.add(4);
        powersOf2.add(8);
        powersOf2.add(16);
        powersOf2.add(32);
        powersOf2.add(64);
        powersOf2.add(128);

        ////// GH_Q1
        // RUMBLE
        // for (Integer power : powersOf2) {
        // benchmarkFiles.add(new FileTuple(
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/GH_Q1/queries/q1_" + power +
        // ".jq",
        // "null",
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/bigghTables/bigghTable"
        // + power),
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/createTables/create_table_"
        // + power + ".jq"),
        // "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/GH_Q1/RUMBLE_" + power + ".txt",
        // false
        // ));
        // }
        //
        // //SPARK
        // String queryFirstHalf = "UPDATE delta.`";
        // String queryLastHalf = "` SET public = (NOT public)";
        // for (Integer power : powersOf2) {
        // benchmarkFiles.add(new FileTuple(
        // "null",
        // queryFirstHalf +
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/bigghTables/bigghTable"
        // + power + queryLastHalf,
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/bigghTables/bigghTable"
        // + power),
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/GH_Q1/createTables/create_table_"
        // + power +".jq"),
        // "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/GH_Q1/SPARK_" + power + ".txt",
        // true
        // ));
        // }

        ////// GH_Q2
        // RUMBLE
        // for (Integer power : powersOf2) {
        // benchmarkFiles.add(new FileTuple(
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/GH_Q2/q2_" + power + ".jq",
        // "null",
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/bigghTables/bigghTable"
        // + power),
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/createTables/create_table_"
        // + power + ".jq"),
        // "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/GH_Q2/RUMBLE_" + power + ".txt",
        // false
        // ));
        // }
        //
        // //SPARK
        // for (Integer power : powersOf2) {
        // benchmarkFiles.add(new FileTuple(
        // "null",
        // (tables) -> {
        // String updateSchemaQuery = "ALTER TABLE delta.`" + tables.get(0) + "` ADD COLUMNS (repo.nickname STRING,
        // repo.popularity_rating INT)";
        // String setQuery = "UPDATE delta.`" + tables.get(0) +"` SET repo.nickname = 'cool_nickname',
        // repo.popularity_rating = -1;";
        // SparkSessionManager.getInstance().getOrCreateSession().sql(updateSchemaQuery);
        // SparkSessionManager.getInstance().getOrCreateSession().sql(setQuery);
        // },
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/bigghTables/bigghTable"
        // + power),
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/createTables/create_table_"
        // + power +".jq"),
        // "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/GH_Q2/SPARK_" + power + ".txt",
        // true
        // ));
        // }

        ////// GH_Q3
        // RUMBLE
        // for (Integer power : powersOf2) {
        // benchmarkFiles.add(new FileTuple(
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/GH_Q3/q3_" + power + ".jq",
        // "null",
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/bigghTables/bigghTable"
        // + power),
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/createTables/create_table_"
        // + power + ".jq"),
        // "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/GH_Q3/RUMBLE_" + power + ".txt",
        // false
        // ));
        // }
        //
        // //SPARK
        // for (Integer power : powersOf2) {
        // benchmarkFiles.add(new FileTuple(
        // "null",
        // (tables) -> {
        // String insertSchemaQuery = "ALTER TABLE delta.`" + tables.get(0) + "` ADD COLUMNS (ids ARRAY<STRUCT<repo_id :
        // STRING, actor_id : STRING, id : STRING>>)";
        // String setQuery = "UPDATE delta.`" + tables.get(0) +"` SET ids = array(named_struct('repo_id', repo.id,
        // 'actor_id', actor.id, 'id', id)), repo.id = NULL, actor.id = NULL, id = NULL;";
        // SparkSessionManager.getInstance().getOrCreateSession().sql(insertSchemaQuery);
        // SparkSessionManager.getInstance().getOrCreateSession().sql(setQuery);
        // },
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/bigghTables/bigghTable"
        // + power),
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/createTables/create_table_"
        // + power + ".jq"),
        // "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/GH_Q3/SPARK_" + power + ".txt",
        // true
        // ));
        // }

        ////// NEW ORDER TRANSACTION
        // RUMBLE
        for (Integer power : powersOf2) {
            this.benchmarkFiles.add(
                new FileTuple(
                        "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/new_order_trans/update_new_order_"
                            + power
                            + ".jq",
                        "null",
                        Collections.singletonList(
                            "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/newOrderTable"
                                + power
                        ),
                        Collections.singletonList(
                            "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/new_order_trans/create_new_order_table_"
                                + power
                                + ".jq"
                        ),
                        "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/NEW_ORDER_TRANS/RUMBLE_NEW_ORDER_INC_"
                            + power
                            + ".txt",
                        false
                )
            );
        }

        // SPARK
        for (Integer power : powersOf2) {
            this.benchmarkFiles.add(
                new FileTuple(
                        null,
                        (tables) -> {
                            String query = "UPDATE delta.`" + tables.get(0) + "` SET NO_O_ID = (NO_O_ID + 1);";
                            SparkSessionManager.getInstance().getOrCreateSession().sql(query);
                        },
                        Collections.singletonList(
                            "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/newOrderTable"
                                + power
                        ),
                        Collections.singletonList(
                            "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/new_order_trans/create_new_order_table_"
                                + power
                                + ".jq"
                        ),
                        "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/NEW_ORDER_TRANS/SPARK_NEW_ORDER_INC_"
                            + power
                            + ".txt",
                        true
                )
            );
        }
        // benchmarkFiles.add(new FileTuple(
        // null,
        // (tables) -> {
        // String query = "WITH updates AS ( SELECT S_I_ID, OL_QUANTITY FROM delta.`" + tables.get(0) +"` JOIN delta.`"
        // + tables.get(1) + "` ON S_I_ID = OL_I_ID AND S_W_ID = OL_SUPPLY_W_ID )" +
        // " MERGE INTO delta.`" + tables.get(0) + "` USING updates" +
        // " ON delta.`" + tables.get(0) + "`.S_I_ID = updates.S_I_ID " +
        // "WHEN MATCHED THEN UPDATE SET S_QUANTITY = CASE WHEN S_QUANTITY - OL_QUANTITY >= 10 THEN S_QUANTITY -
        // OL_QUANTITY ELSE S_QUANTITY - OL_QUANTITY + 91," +
        // " S_YTD = S_YTD + OL_QUANTITY, S_ORDER_CNT = S_ORDER_CNT + 1";
        // SparkSessionManager.getInstance().getOrCreateSession().sql(query);
        // },
        // Arrays.asList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/stockTable32",
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/orderLineTable"),
        // Arrays.asList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/new_order_trans/create_stock_table_32.jq",
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/new_order_trans/create_order_line_table.jq"),
        // "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/NEW_ORDER_TRANS/SPARK_STOCK_OL_UP.txt",
        // true
        // ));

        ////// PAYMENT TRANSACTION
        // RUMBLE
        // for (Integer power : powersOf2) {
        // benchmarkFiles.add(new FileTuple(
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/payment_trans/payment" +
        // power + ".jq",
        // "null",
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/customerTable"
        // + power),
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/payment_trans/create_customer_table_"
        // + power + ".jq"),
        // "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/PAYMENT_TRANS/RUMBLE_CUSTOMER_IF_" + power + ".txt",
        // false
        // ));
        // }
        //
        // // SPARK
        // for (Integer power : powersOf2) {
        // benchmarkFiles.add(new FileTuple(
        // null,
        // (tables) -> {
        // int h_amt = 343;
        // String query = "UPDATE delta.`" + tables.get(0) +"` SET C_BALANCE = (C_BALANCE + " + h_amt + "), C_DATA =
        // if(C_CREDIT == 'BC', concat(C_DATA, C_ID, C_D_ID, C_W_ID, " + h_amt + "), C_DATA);";
        // SparkSessionManager.getInstance().getOrCreateSession().sql(query);
        // },
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/customerTable"
        // + power),
        // Collections.singletonList("/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/payment_trans/create_customer_table_"
        // + power + ".jq"),
        // "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/PAYMENT_TRANS/SPARK_CUSTOMER_IF_" + power + ".txt",
        // true
        // ));
        // }

        ////// DELIVERY TRANSACTION
        // RUMBLE
        // for (Integer power : powersOf2) {
        // benchmarkFiles.add(new FileTuple(
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/delivery_trans/delivery" +
        // power + ".jq",
        // "null",
        // Arrays.asList(
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/customerTable" + power,
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/districtTable",
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/orderLineTable"
        // ),
        // Arrays.asList(
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/delivery_trans/create_customer_table_"
        // + power + ".jq",
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/delivery_trans/create_district_table.jq",
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/delivery_trans/create_order_line_table.jq"
        // ),
        // "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/DELIVERY_TRANS/RUMBLE_CUSTOMER_JOIN_" + power +
        // ".txt",
        // false
        // ));
        // }
        //
        // // SPARK
        // for (Integer power : powersOf2) {
        // benchmarkFiles.add(new FileTuple(
        // null,
        // (tables) -> {
        // int no_o_id = 4;
        // int w_id = 1;
        // String query = "WITH ol_total_per_dist AS ( " +
        // "SELECT D_ID, SUM(OL_AMOUNT) AS OL_TOTAL FROM delta.`" + tables.get(1) + "` JOIN delta.`" + tables.get(2) +
        // "` ON D_ID = OL_D_ID" +
        // " WHERE OL_W_ID = " + w_id + " AND OL_O_ID = " + no_o_id +
        // " GROUP BY D_ID" +
        // ")" +
        // " MERGE INTO delta.`" + tables.get(0) +"` USING ol_total_per_dist" +
        // " ON delta.`" + tables.get(0) +"`.C_D_ID = ol_total_per_dist.D_ID" +
        // " WHEN MATCHED THEN UPDATE SET C_BALANCE = (C_BALANCE + ol_total_per_dist.OL_TOTAL);";
        // SparkSessionManager.getInstance().getOrCreateSession().sql(query);
        // },
        // Arrays.asList(
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/customerTable" + power,
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/districtTable",
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta_benchmark_data/orderLineTable"
        // ),
        // Arrays.asList(
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/delivery_trans/create_customer_table_"
        // + power + ".jq",
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/delivery_trans/create_district_table.jq",
        // "/home/davidl/Documents/Thesis/rumble/src/test/resources/queries/delta-benchmark/delivery_trans/create_order_line_table.jq"
        // ),
        // "/home/davidl/Documents/Thesis/rumble/BenchmarkResults/DELIVERY_TRANS/SPARK_CUSTOMER_JOIN_" + power + ".txt",
        // true
        // ));
        // }

    }

    public static void setupSparkSession() {
        SparkSessionManager.getInstance().resetSession();
        System.err.println("Java version: " + javaVersion);
        System.err.println("Scala version: " + scalaVersion);
        SparkConf sparkConfiguration = new SparkConf();
        if (sparkConfiguration.get("spark.app.name", "<none>").equals("<none")) {
            LogManager.getLogger("SparkSessionManager")
                .warn(
                    "No app name specified (you can do so with --conf spark.app.name=your_name). Setting to "
                        + APP_NAME
                );
            sparkConfiguration.setAppName(APP_NAME);
        }
        sparkConfiguration.set("spark.master", "local[*]");
        // sparkConfiguration.setMaster("local[*]");
        // sparkConfiguration.set("spark.submit.deployMode", "client");
        sparkConfiguration.set("spark.sql.crossJoin.enabled", "true"); // enables cartesian product
        sparkConfiguration.set("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension"); // enables delta
        // store
        sparkConfiguration.set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog"); // enables
        // delta
        // store

        // prevents spark from failing to start on MacOS when disconnected from the internet
        sparkConfiguration.set("spark.driver.host", "127.0.0.1");


        sparkConfiguration.set("spark.kryoserializer.buffer.max", "256m");
        sparkConfiguration.set("spark.driver.memory", "4g");
        sparkConfiguration.set("spark.executor.memory", "4g");
        sparkConfiguration.set("spark.python.worker.memory", "4g");
        // sparkConfiguration.set("spark.speculation", "true");
        // sparkConfiguration.set("spark.speculation.quantile", "0.5");
        SparkSessionManager.getInstance().initializeConfigurationAndSession(sparkConfiguration, true);
        SparkSessionManager.COLLECT_ITEM_LIMIT = configuration.getResultSizeCap();
        System.err.println("Spark version: " + SparkSessionManager.getInstance().getJavaSparkContext().version());
    }

    public List<Item> benchmarkDeltaTest(Rumble rumble, URI uri) throws IOException {
        SequenceOfItems sequence = rumble.runQuery(uri);
        List<Item> res = new ArrayList<>();
        sequence.populateList(res);
        return res;
    }

    public void benchmarkDelta(
            String queryPath,
            List<String> tablePaths,
            List<String> createTablePaths,
            String outputPath
    )
            throws IOException {
        long total = 0;
        long[] diffs = new long[10];
        long start;
        long end;
        long diff;
        List<Item> res;

        this.appendToFile(outputPath, "RUMBLE");
        this.appendToFile(outputPath, queryPath);

        URI uri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
            queryPath,
            configuration,
            ExceptionMetadata.EMPTY_METADATA
        );
        Rumble rumble = new Rumble(configuration);

        // WARMUP
        for (int i = 0; i < 3; i++) {
            this.createTables(tablePaths, createTablePaths);
            res = this.benchmarkDeltaTest(rumble, uri);
            this.deleteTables(tablePaths);
        }
        this.appendToFile(outputPath, "WARMUP DONE");

        // TIMING
        for (int i = 0; i < 10; i++) {
            this.createTables(tablePaths, createTablePaths);
            start = System.nanoTime();
            res = this.benchmarkDeltaTest(rumble, uri);
            end = System.nanoTime();
            this.deleteTables(tablePaths);
            diff = (end - start) / 1000000;
            diffs[i] = diff;
            total += diff;
            this.appendToFile(outputPath, "ITERATION" + i);
            this.appendToFile(outputPath, String.valueOf(diff));
        }

        this.appendToFile(outputPath, "##########################################");
        this.appendToFile(outputPath, "DONE");
        this.appendToFile(outputPath, queryPath);
        this.appendToFile(outputPath, "MS");
        this.appendToFile(outputPath, "TOTAL: " + total);
        this.appendToFile(outputPath, "DIFFS: " + Arrays.toString(diffs));
        this.appendToFile(outputPath, "AVG: " + (total / 10));
        this.appendToFile(outputPath, "SD: " + sd(diffs));
        this.appendToFile(outputPath, "##########################################");
    }


    public void benchmarkSparkSQLTest(String query) {
        SparkSessionManager.getInstance().getOrCreateSession().sql(query);
    }

    public void benchmarkSpark(
            String query,
            List<String> tablePaths,
            List<String> createTablePaths,
            String outputPath,
            Optional<Consumer<List<String>>> possSqlFunc
    )
            throws IOException {
        long total = 0;
        long[] diffs = new long[10];
        long start;
        long end;
        long diff;

        this.appendToFile(outputPath, "SPARK");
        for (String tablePath : tablePaths) {
            this.appendToFile(outputPath, tablePath);
        }

        Consumer<List<String>> func = null;
        if (possSqlFunc.isPresent()) {
            func = possSqlFunc.get();
        }

        // WARMUP
        for (int i = 0; i < 3; i++) {
            this.createTables(tablePaths, createTablePaths);
            if (func != null) {
                func.accept(tablePaths);
            } else {
                this.benchmarkSparkSQLTest(query);
            }
            this.deleteTables(tablePaths);
        }
        this.appendToFile(outputPath, "WARMUP DONE");

        // TIMING
        if (func != null) {
            for (int i = 0; i < 10; i++) {
                this.createTables(tablePaths, createTablePaths);
                start = System.nanoTime();
                func.accept(tablePaths);
                end = System.nanoTime();
                this.deleteTables(tablePaths);
                diff = (end - start) / 1000000;
                diffs[i] = diff;
                total += diff;
                this.appendToFile(outputPath, "ITERATION" + i);
                this.appendToFile(outputPath, String.valueOf(diff));
            }
        } else {
            for (int i = 0; i < 10; i++) {
                this.createTables(tablePaths, createTablePaths);
                start = System.nanoTime();
                this.benchmarkSparkSQLTest(query);
                end = System.nanoTime();
                this.deleteTables(tablePaths);
                diff = (end - start) / 1000000;
                diffs[i] = diff;
                total += diff;
                this.appendToFile(outputPath, "ITERATION" + i);
                this.appendToFile(outputPath, String.valueOf(diff));
            }
        }

        this.appendToFile(outputPath, "##########################################");
        this.appendToFile(outputPath, "DONE");
        for (String tablePath : tablePaths) {
            this.appendToFile(outputPath, tablePath);
        }
        this.appendToFile(outputPath, "MS");
        this.appendToFile(outputPath, "TOTAL: " + total);
        this.appendToFile(outputPath, "DIFFS: " + Arrays.toString(diffs));
        this.appendToFile(outputPath, "AVG: " + (total / 10));
        this.appendToFile(outputPath, "SD: " + sd(diffs));
        this.appendToFile(outputPath, "##########################################");
    }

    public void appendToFile(String path, String str) {
        try (
            FileWriter writer = new FileWriter(path, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer)
        ) {
            bufferedWriter.write(str);
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable(String path, String query) throws IOException {
        URI tableURI = FileSystemUtil.resolveURIAgainstWorkingDirectory(
            path,
            DeltaUpdateRuntimeTests.createDeltaConfiguration,
            ExceptionMetadata.EMPTY_METADATA
        );
        URI queryURI = FileSystemUtil.resolveURIAgainstWorkingDirectory(
            query,
            DeltaUpdateRuntimeTests.createDeltaConfiguration,
            ExceptionMetadata.EMPTY_METADATA
        );

        UpdatesForRumbleBenchmark.createDeltaConfiguration.setOutputPath(tableURI.getPath());
        UpdatesForRumbleBenchmark.createDeltaConfiguration.setQueryPath(queryURI.getPath());
        JsoniqQueryExecutor executor = new JsoniqQueryExecutor(UpdatesForRumbleBenchmark.createDeltaConfiguration);
        executor.runQuery();
    }

    public void createTables(List<String> paths, List<String> queries) throws IOException {
        for (int i = 0; i < paths.size(); i++) {
            this.createTable(paths.get(i), queries.get(i));
        }
    }

    public void deleteTable(String path) {
        URI tableURI = FileSystemUtil.resolveURIAgainstWorkingDirectory(
            path,
            UpdatesForRumbleBenchmark.createDeltaConfiguration,
            ExceptionMetadata.EMPTY_METADATA
        );

        try {
            File oldTable = new File(tableURI.getPath());
            FileUtils.deleteDirectory(oldTable);
            System.err.println("Deleted file: " + oldTable.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void deleteTables(List<String> paths) {
        for (String path : paths) {
            this.deleteTable(path);
        }
    }

    public static double sd(long[] arr) {
        double sd = 0.0;

        long sum = Arrays.stream(arr).sum();
        long N = arr.length;
        double mean = (double) sum / N;

        for (long x : arr) {
            sd += Math.pow(x - mean, 2);
        }

        return Math.sqrt(sd / N);
    }


    public static void main(String[] args) throws IOException {

        UpdatesForRumbleBenchmark benchmark = new UpdatesForRumbleBenchmark();
        setupSparkSession();

        for (FileTuple ft : benchmark.benchmarkFiles) {
            if (ft.isSQL()) {
                benchmark.benchmarkSpark(
                    ft.getQueryMaterial(),
                    ft.getTablePaths(),
                    ft.getCreateTablePaths(),
                    ft.getOutputPath(),
                    Optional.ofNullable(ft.getSqlQueryFunc())
                );
            } else {
                benchmark.benchmarkDelta(
                    ft.getQueryMaterial(),
                    ft.getTablePaths(),
                    ft.getCreateTablePaths(),
                    ft.getOutputPath()
                );
            }
        }

        System.out.println("##########################################");
        System.out.println("DONE");
        System.out.println("##########################################");

    }

    class FileTuple {

        private String queryPath;
        private String sqlQuery;
        private Consumer<List<String>> sqlQueryFunc;
        private List<String> tablePaths;
        private List<String> createTablePaths;
        private String outputPath;
        private boolean isSQL;

        public FileTuple(
                String queryPath,
                String sqlQuery,
                List<String> tablePaths,
                List<String> createTablePaths,
                String outputPath,
                boolean isSQL
        ) {
            this.queryPath = queryPath;
            this.sqlQuery = sqlQuery;
            this.tablePaths = tablePaths;
            this.createTablePaths = createTablePaths;
            this.outputPath = outputPath;
            this.isSQL = isSQL;
        }

        public FileTuple(
                String queryPath,
                Consumer<List<String>> sqlQueryFunc,
                List<String> tablePaths,
                List<String> createTablePaths,
                String outputPath,
                boolean isSQL
        ) {
            this.queryPath = queryPath;
            this.sqlQuery = null;
            this.sqlQueryFunc = sqlQueryFunc;
            this.tablePaths = tablePaths;
            this.createTablePaths = createTablePaths;
            this.outputPath = outputPath;
            this.isSQL = isSQL;
        }

        public String getQueryMaterial() {
            return this.isSQL() ? this.sqlQuery : this.queryPath;
        }

        public Consumer<List<String>> getSqlQueryFunc() {
            return this.sqlQueryFunc;
        }

        public List<String> getTablePaths() {
            return this.tablePaths;
        }

        public List<String> getCreateTablePaths() {
            return this.createTablePaths;
        }

        public String getOutputPath() {
            return this.outputPath;
        }

        public boolean isSQL() {
            return this.isSQL;
        }
    }
}
