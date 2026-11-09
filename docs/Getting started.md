# Getting Started

Below, you will find instructions to get started with RumbleDB, either in an online sandbox or on your own computer, which among others will allow you to query any files stored on your local disk. In short, there are four possibilities to get started:

- Use one of our online sandboxes (Jupyter notebook or simple sandbox page)
- Run the standalone RumbleDB jar (new and experimental) with Java on your laptop
- Install Spark yourself on your laptop (for more control on Spark parameters) and use a small RumbleDB jar with spark-submit
- Use our docker image on your laptop (go to the "Run with docker" section on the left menu)

## Method 0: you want to play with RumbleDB without installing anything

If you really want to start writing queries right now, there is a public sandbox [here](https://colab.research.google.com/github/RumbleDB/rumble/blob/master/RumbleSandbox.ipynb) that will just work and guide you. You only need to have a Google account to be able to execute them, as this exposes our Jupyter notebook via the Colab environment. You are also free to download and use this notebook with any other provider or even your own local Jupyter and it will work just the same: the queries are all shipped to our own, small public backend no matter what.

If you do not have a Google account, you can also use our simpler sandbox page without Jupyter, [here](http://public.rumbledb.org:9090/public.html) where you can type small queries and see the results.

With the sandboxes above, you can only inline your data in the query or access a dataset with an HTTP URL.

Once you want to take it to the next level and query your own data on your laptop, you will find instructions below to use RumbleDB on your own computer manually, which among others will allow you to query any files stored on your local disk. And then, you can take a leap of faith and use RumbleDB on a large cluster (Amazon EMR, your company's cluster, etc).

## Method 1: with the large, standalone, RumbleDB jar (experimental)

### Java version (important)

You need to make sure that you have Java 11 or 17 and that, if you have several versions installed, JAVA_HOME correctly points to Java 11 or 17.

RumbleDB works with both Java 11 and Java 17. You can check the Java version that is configured on your machine with:

    java -version

If you do not have Java, you can download version 11 or 17 from [AdoptOpenJDK](https://adoptopenjdk.net/).

Do make sure it is not Java 8, which will not work.

### Download RumbleDB

RumbleDB is just a download and no installation is required.

In order to run RumbleDB, you simply need to download rumbledb-1.24.0-standalone.jar from the [download page](https://github.com/RumbleDB/rumble/releases) and put it in a directory of your choice, for example, right besides your data.

Make sure to use the corresponding jar name accordingly in all our instructions in lieu of rumbledb.jar.

You can test that it works with:

    java -jar rumbledb-1.24.0-standalone.jar run -q '1+1'

or launch a JSONiq shell with:

    java -jar rumbledb-1.24.0-standalone.jar repl
    
If you run out of memory, you can set allocate more memory to Java with an additional Java parameter, e.g., -Xmx10g

Scroll down this page skipping the method 2 section in order to continue.

## Method 1 bis: with Homebrew

It is also possible to use RumbleDB with brew, however there is currently no way to adjust memory usage. To install RumbleDB with brew, type the commands:

    brew tap rumbledb/rumble
    brew install --build-from-source rumble

You can test that it works with:

    rumbledb run -q '1+1'

Then, launch a JSONiq shell with:

    rumbledb repl

Scroll down this page skipping the method 2 section in order to continue.

## Method 2: Install Spark locally yourself and use a compact RumbleDB jar

This method gives you more control about the Spark configuration than the experimental standalone jar, in particular you can increase the memory used, change the number of cores, and so on.

If you use Linux, Florian Kellner also kindly contributed an [installation script](https://github.com/fkellner/rumbledb-install-script) for Linux users that roughly takes care of what is described below for you.

### Install Spark

RumbleDB requires an Apache Spark installation on Linux, Mac or Windows. Important note: it needs to be the scala 2.13 version of spark as RumbleDB supports only that version.

It is straightforward to directly [download it](https://spark.apache.org/downloads.html), unpack it and put it at a location of your choosing. We recommend to pick Spark 3.5.5. Let us call this location SPARK_HOME (it is a good idea, in fact to also define an environment variable SPARK_HOME pointing to the absolute path of this location).

What you need to do then is to add the subdirectory "bin" within the unpacked directory to the PATH variable. On macOS this is done by adding

    export SPARK_HOME=/path/to/spark-3.5.5-bin-hadoop3-scala2.13
    export PATH=$SPARK_HOME/bin:$PATH

(with SPARK_HOME appropriately set to match your unzipped Spark directory) to the file .zshrc in your home directory, then making sure to force the change with

    . ~/.zshrc

in the shell. In Windows, changing the PATH variable is done in the control panel. In Linux, it is similar to macOS.

As an alternative, users who love the command line can also install Spark with a package management system instead, such as brew (on macOS) or apt-get (on Ubuntu). However, these might be less predictable than a raw download.

You can test that Spark was correctly installed with:

    spark-submit --version
   
### Java version (important)

You need to make sure that you have Java 11 or 17 and that, if you have several versions installed, JAVA_HOME correctly points to Java 11 or 17. Spark only supports Java 11 or 17.

Spark 3+ is documented to work with both Java 11 and Java 17. If there is an issue with the Java version, RumbleDB will inform you with an appropriate error message. You can check the Java version that is configured on your machine with:

    java -version

### Download the small version of the RumbleDB jar

Like Spark, RumbleDB is just a download and no installation is required.

In order to run RumbleDB, you simply need to download one of the small .jar files from the [download page](https://github.com/RumbleDB/rumble/releases) and put it in a directory of your choice, for example, right besides your data.

If you use Spark 3.5, use rumbledb-1.24.0-for-spark-3.5.jar.

If you use Spark 4.0, use rumbledb-1.24.0-for-spark-4.0.jar.

These jars do not embed Spark, since you chose to set it up separately. They will work with your Spark installation with the spark-submit command.

Make sure to use the corresponding jar name accordingly in all our instructions in lieu of rumbledb.jar.

## Create some data set

Create, in the same directory as RumbleDB to keep it simple, a file data.json and put the following content inside. This is a small list of JSON objects in the JSON Lines format.

    { "product" : "broiler", "store number" : 1, "quantity" : 20  }
    { "product" : "toaster", "store number" : 2, "quantity" : 100 }
    { "product" : "toaster", "store number" : 2, "quantity" : 50 }
    { "product" : "toaster", "store number" : 3, "quantity" : 50 }
    { "product" : "blender", "store number" : 3, "quantity" : 100 }
    { "product" : "blender", "store number" : 3, "quantity" : 150 }
    { "product" : "socks", "store number" : 1, "quantity" : 500 }
    { "product" : "socks", "store number" : 2, "quantity" : 10 }
    { "product" : "shirt", "store number" : 3, "quantity" : 10 }
    
If you want to later try a bigger version of this data, you can also download a larger version with 100,000 objects from [here](https://rumbledb.org/samples/products-small.json). Wait, no, in fact you do not even need to download it: you can simply replace the file path in the queries below with "https://rumbledb.org/samples/products-small.json" and it will just work! RumbleDB feels just at home on the Web.

RumbleDB also scales without any problems to datasets that have millions or (on a cluster) billions of objects, although of course, for billions of objects HDFS or S3 are a better idea than the Web to store your data, for obvious reasons.

In the JSON Lines format that this simple dataset uses, you just need to make sure you have one object on each line (this is different from a plain JSON file, which has a single JSON value and can be indented). Of course, RumbleDB can read plain JSON files, too (with json-doc()), but below we will show you how to read JSON Line files, which is how JSON data scales.

## Running simple queries locally

If you used installation method 1 (the standalone jar), in a shell, from the directory where the RumbleDB .jar lies, type, all on one line:

    java -jar rumbledb.jar repl

replacing rumbledb.jar with the actual name of the jar file you downloaded.

If you used installation method 2 (manual Spark setup), in a shell, from the directory where the RumbleDB .jar lies, type, all on one line:

    spark-submit rumbledb.jar repl

replacing rumbledb.jar with the actual name of the jar file you downloaded.

The RumbleDB shell appears:

        ____                  __    __     ____  ____ 
       / __ \__  ______ ___  / /_  / /__  / __ \/ __ )
      / /_/ / / / / __ `__ \/ __ \/ / _ \/ / / / __  |  The distributed JSONiq engine
     / _, _/ /_/ / / / / / / /_/ / /  __/ /_/ / /_/ /   1.24.0 "Lemon Ironwood" beta
    /_/ |_|\__,_/_/ /_/ /_/_.___/_/\___/_____/_____/  

    
    Master: local[*]
    Item Display Limit: 200
    Output Path: -
    Log Path: -
    Query Path : -

    rumble$
    
You can now start typing simple queries like the following few examples. Press *three times* the return key to execute a query.

    "Hello, World"
    
or
 
     1 + 1
     
or
 
     (3 * 4) div 5
     
The above queries do not actually use Spark. Spark is used when the I/O workload can be parallelized. The following query should output the file created above.
     
     json-lines("data.json")
     
json-lines() reads its input in parallel, and thus will also work on your machine with MB or GB files (for TB files, a cluster will be preferable). You should specify a minimum number of partitions, here 10 (note that this is a bit ridiculous for our tiny example, but it is very relevant for larger files), as locally no parallelization will happen if you do not specify this number.

    for $i in json-lines("data.json", 10)
    return $i

The above creates a very simple Spark job and executes it. More complex queries will create several Spark jobs. But you will not see anything of it: this is all done behind the scenes. If you are curious, you can go to [localhost:4040](http://localhost:4040) in your browser while your query is running (it will not be available once the job is complete) and look at what is going on behind the scenes.

Data can be filtered with the where clause. Again, below the hood, a Spark transformation will be used:

    for $i in json-lines("data.json", 10)
    where $i.quantity gt 99
    return $i
    
RumbleDB also supports grouping and aggregation, like so:

    for $i in json-lines("data.json", 10)
    let $quantity := $i.quantity
    group by $product := $i.product
    return { "product" : $product, "total-quantity" : sum($quantity) }
    

RumbleDB also supports ordering. Note that clauses (where, let, group by, order by) can appear in any order.
The only constraint is that the first clause should be a for or a let clause.

    for $i in json-lines("data.json", 10)
    let $quantity := $i.quantity
    group by $product := $i.product
    let $sum := sum($quantity)
    order by $sum descending
    return { "product" : $product, "total-quantity" : $sum }

Finally, RumbleDB can also parallelize data provided within the query, exactly like Sparks' parallelize() creation:

    for $i in parallelize((
     { "product" : "broiler", "store number" : 1, "quantity" : 20  },
     { "product" : "toaster", "store number" : 2, "quantity" : 100 },
     { "product" : "toaster", "store number" : 2, "quantity" : 50 },
     { "product" : "toaster", "store number" : 3, "quantity" : 50 },
     { "product" : "blender", "store number" : 3, "quantity" : 100 },
     { "product" : "blender", "store number" : 3, "quantity" : 150 },
     { "product" : "socks", "store number" : 1, "quantity" : 500 },
     { "product" : "socks", "store number" : 2, "quantity" : 10 },
     { "product" : "shirt", "store number" : 3, "quantity" : 10 }
    ), 10)
    let $quantity := $i.quantity
    group by $product := $i.product
    let $sum := sum($quantity)
    order by $sum descending
    return { "product" : $product, "total-quantity" : $sum }

Mind the double parenthesis, as parallelize is a unary function to which we pass a sequence of objects.

## Further steps

Further steps could involve:

- Learning JSONiq. More details can be found in the JSONiq section of this documentation and in the [JSONiq specification](https://www.jsoniq.org/docs/JSONiq/webhelp/index.html) and [tutorials](https://colab.research.google.com/github/RumbleDB/rumble/blob/master/RumbleSandbox.ipynb).
- Storing some data on S3, creating a Spark cluster on Amazon EMR (or Azure blob storage and Azure, etc), and querying the data with RumbleDB. More details are found in the cluster section of this documentation.
- Using RumbleDB with Jupyter notebooks. For this, you can run RumbleDB as a server with a simple command, and get started by downloading the [main JSONiq tutorial as a Jupyter notebook](https://raw.githubusercontent.com/RumbleDB/rumble/master/RumbleSandbox.ipynb) and just clicking your way through it. More details are found in the Jupyter notebook section of this documentation. Jupyter notebooks work both locally and on a cluster.
- Write JSONiq code, and share it on the Web, as others can import it from HTTP in just one line from within their queries (no package publication or installation required) or specify an HTTP URL as an input query to RumbleDB!


