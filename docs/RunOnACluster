# Running Sparksoniq on a cluster

After you have tried Sparksoniq locally as explained in the getting started section, you can take Sparksoniq to a real cluster
simply by modifying the command line parameters as documented [here for spark-submit](https://spark.apache.org/docs/latest/submitting-applications.html).
You can also adapt the number of executors, etc.

## Creation functions

json-file() then takes an HDFS path and the host and port are optional if Spark is configured properly. A second parameter controls the number of splits.
The same goes for parallelize()

## Bigger data sets

If you need a bigger data set out of the box, we recommend the [great language game](http://lars.yencken.org/datasets/languagegame/), which has 16 million objects.
We tested it successfully and suggest the following queries to start with:

    for $i in json−file(”hdfs://confusion−2014−03−02.json”, 300) let $guess := $i.guess
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

Note that by default only the first 1000 items in the output will be displayed on the shell, but you can change it with the --result-size parameter on the CLI.

## Execution of single queries and output to HDFS

Sparksoniq also supports executing a single query from the command line, reading from HDFS and outputting the results to HDFS, with the query file being either local or on HDFS. We will soon document this as well.
