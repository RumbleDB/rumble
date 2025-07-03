# Running RumbleDB on a cluster

After you have tried RumbleDB locally as explained in the getting started section, you can take RumbleDB to a real cluster
simply by modifying the command line parameters as documented [here for spark-submit](https://spark.apache.org/docs/latest/submitting-applications.html).

# Creating a cluster

Creating a cluster is the easiest part, as most cloud providers today offer that with just a few clicks: Amazon EMR, Azure HDInsight, etc. You can start with 4-5 machines with a few CPUs each and a bit of memory, and increase later when you want to get serious on larger scales.

Maybe sure to select a cluster that has Apache Spark. On Amazon EMR, this is not the default and you need to make sure that you check the box that has Spark below the cluster version dropdown. We recommend taking the latest EMR version 6.5.0 and then picking Spark 3.1 in the software configuration. You will also need to create a public/private key pair if you do not already have one.

Wait for 5 or 6 minutes, and the cluster is ready.

Do not forget to terminate the cluster when you are done!

# How to tune the RumbleDB command

Next, you need to use ssh to connect to the master node of your cluster as the hadoop user and specifying your private key file. You will find the hostname of the machine on the EMR cluster page. The command looks like:

ssh -i ~/.ssh/yourkey.pem hadoop@ec2-a-bunch-if-numbers.eu-central-1.compute.amazonaws.com

If ssh hangs, then you may need to authorize your IP for incoming connections in the security group of your cluster.

And once you have connected with ssh and are on the shell, you can start using RumbleDB in a way similar to what you do on your laptop.

First you need to download it with wget (which is usually available by default on cloud virtual machines):

    wget https://github.com/RumbleDB/rumble/releases/download/v1.24.0/rumbledb-1.24.0-for-spark-3.5.jar

This is all you need to do, since Apache Spark is already installed. If spark-submit does not work, you might want to wait for a few more minutes as it might be that the cluster is not fully prepared yet.

Often, the Spark cluster is running on yarn. The --master option can be changed from local[\*] (which was for running on your laptop) to yarn compared to the getting started guide.

    spark-submit --master yarn --deploy-mode client rumbledb-1.24.0-for-spark-3.5.jar repl
                 
Most of the time, though (e.g., on Amazon EMR), it needs not be specified, as this is already set up in the environment. So the same command will do:

    spark-submit rumbledb-1.24.0-for-spark-3.5.jar repl
                 
When you are on a cluster, you can also adapt the number of executors, how many cores you want per executor, etc. It is recommended to use sqrt(n) cores per executor if a node has n cores. For the executor memory, it is just primary school math: you need to divide the memory on a machine with the number of executors per machine (which is also roughly sqrt(n)).

For example, if we have 6 worker nodes with each 16 cores and 64 GB, we can use 5 executores on each machine, with 3 cores and 10 GB per executor. This leaves a core and a bit of memory free for other cluster tasks.

    spark-submit --num-executors 30 --executor-cores 3 --executor-memory 10g
                 rumbledb-1.24.0-for-spark-3.5.jar repl

If necesasry, the size limit for materialization can be made higher with --materialization-cap or its shortcut -c (the default is 200). This affects the number of items displayed on the shells as an answer to a query. It also affects the maximum number of items that can be materialized from a large sequence into, say, an array. Warnings are issued if the cap is reached.

    spark-submit --num-executors 30 --executor-cores 3 --executor-memory 10g
                 rumbledb-1.24.0-for-spark-3.5.jar repl -c 10000

## Creation functions

json-lines() then takes an HDFS path and the host and port are optional if Spark is configured properly. A second parameter controls the minimum number of splits. By default, each HDFS block is a split if executed on a clustter. In a local execution, there is only one split by default.

The same goes for parallelize(). It is also possible to read text with text-file(), parquet files with parquet-file(), and it is also possible to read data on S3 rather than HDFS for all three functions json-lines(), text-file() and parquet-file().

## Bigger data sets

If you need a bigger data set out of the box, we recommend the [great language game](http://lars.yencken.org/datasets/languagegame/), which has 16 million objects. On Amazon EMR, we could even read several billion of objects on less than ten machines.

We tested this with each new release, and suggest the following queries to start with (we assume HDFS is the default file system, and that you copied over this dataset to HDFS with hadoop fs copyFromLocal):

    for $i in json-lines(”/user/you/confusion−2014−03−02.json”, 300)
    let $guess := $i.guess
    let $target := $i.target
    where $guess eq $target
    where $target eq ”Russian”
    return $i
    
    for $i in json-lines(”/user/you/confusion−2014−03−02.json”, 300)
    let $guess := $i.guess, $target := $i.target
    where $guess eq $target
    order by $target, $i.country descending, $i.date descending return $i
    
    for $i in json-lines(”/user/you/confusion−2014−03−02.json”, 300)
    let $country := $i.country, $target := $i.target
    group by $target , $country
    return {”Language”: $target ,
    ”Country” : $country , ”Guesses”: length($i)}

Note that by default only the first 200 items in the output will be displayed on the shell, but you can change it with the --materialization-cap parameter on the CLI.

## Execution of single queries and output to HDFS

RumbleDB also supports executing a single query from the command line, reading from HDFS and outputting the results to HDFS, with the query file being either local or on HDFS. For this, use the --query-path (optional as any text without parameter is recognized as a path in any case), --output-path (shortcut -o) and --log-path parameters.

    spark-submit --num-executors 30 --executor-cores 3 --executor-memory 10g
                 rumbledb-1.24.0-for-spark-3.5.jar run "hdfs:///user/me/query.jq"
                 -o "hdfs:///user/me/results/output"
                 --log-path "hdfs:///user/me/logging/mylog"

The query path, output path and log path can be any of the supported schemes (HDFS, file, S3, WASB...) and can be relative or absolute.

    spark-submit --num-executors 30 --executor-cores 3 --executor-memory 10g
                 rumbledb-1.24.0-for-spark-3.5.jar run "/home/me/my-local-machine/query.jq"
                 -o "/user/me/results/output"
                 --log-path "hdfs:///user/me/logging/mylog"

