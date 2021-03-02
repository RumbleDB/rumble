# Running Rumble on a cluster

After you have tried Rumble locally as explained in the getting started section, you can take Rumble to a real cluster
simply by modifying the command line parameters as documented [here for spark-submit](https://spark.apache.org/docs/latest/submitting-applications.html).

If the Spark cluster is running on yarn, then the --master option can be changed from local[\*] to yarn compared to the getting started guide. Most of the time, though (e.g., on Amazon EMR), it needs not be specified, as this is already set up in the environment.

    spark-submit spark-rumble-1.11.0.jar --shell yes
                 
or explicitly:
                 
    spark-submit --master yarn --deploy-mode client spark-rumble-1.11.0.jar --shell yes
                 
You can also adapt the number of executors, etc.

    spark-submit --num-executors 30 --executor-cores 3 --executor-memory 10g
                 spark-rumble-1.11.0.jar --shell yes

The size limit for materialization can also be made higher with --materialization-cap (the default is 200). This affects the number of items displayed on the shells as an answer to a query, as well as any materializations happening within the query with push-down is not supported. Warnings are issued if the cap is reached.

    spark-submit --num-executors 30 --executor-cores 3 --executor-memory 10g
                 spark-rumble-1.11.0.jar
                 --shell yes --materialization-cap 10000

## Creation functions

json-file() then takes an HDFS path and the host and port are optional if Spark is configured properly. A second parameter controls the minimum number of splits. By default, each HDFS block is a split if executed on a clustter. In a local execution, there is only one split by default.

The same goes for parallelize(). It is also possible to read text with text-file(), parquet files with parquet-file(), and it is also possible to read data on S3 rather than HDFS for all three functions json-file(), text-file() and parquet-file().

## Bigger data sets

If you need a bigger data set out of the box, we recommend the [great language game](http://lars.yencken.org/datasets/languagegame/), which has 16 million objects. On Amazon EMR, we could even read several billion of objects on less than ten machines.

We tested it successfully and suggest the following queries to start with:

    for $i in json−file(”hdfs://confusion−2014−03−02.json”, 300)
    let $guess := $i.guess
    let $target := $i.target
    where $guess eq $target
    where $target eq ”Russian”
    return $i
    
    for $i in json−file(”hdfs://confusion−2014−03−02.json”, 300)
    let $guess := $i.guess, $target := $i.target
    where $guess eq $target
    order by $target, $i.country descending, $i.date descending return $i
    
    for $i in json−file(”hdfs://confusion−2014−03−02.json”, 300)
    let $country := $i.country, $target := $i.target
    group by $target , $country
    return {”Language”: $target ,
    ”Country” : $country , ”Guesses”: length($i)}

Note that by default only the first 1000 items in the output will be displayed on the shell, but you can change it with the --materialization-cap parameter on the CLI.

## Execution of single queries and output to HDFS

Rumble also supports executing a single query from the command line, reading from HDFS and outputting the results to HDFS, with the query file being either local or on HDFS. For this, use the --query-path, --output-path and --log-path parameters.

    spark-submit --num-executors 30 --executor-cores 3 --executor-memory 10g
                 spark-rumble-1.11.0.jar
                 --query-path "hdfs:///user/me/query.jq"
                 --output-path "hdfs:///user/me/results/output"
                 --log-path "hdfs:///user/me/logging/mylog"

The query path, output path and log path can be any of the supported schemes (HDFS, file, S3, WASB...) and can be relative or absolute.

    spark-submit --num-executors 30 --executor-cores 3 --executor-memory 10g
                 spark-rumble-1.11.0.jar
                 --query-path "/home/me/my-local-machine/query.jq"
                 --output-path "/user/me/results/output"
                 --log-path "hdfs:///user/me/logging/mylog"

