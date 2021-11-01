# Getting Started

## Prerequisites

### Install Spark

RumbleDB requires a Spark installation on Linux, Mac or Windows.

Users who love the command line can install Spark with a package management system, such as brew (on macOS) or apt-get (on Ubuntu).

However, it is also straightforward to directly [download it](https://spark.apache.org/downloads.html), unpack it, and add the subdirectory "bin" within the unpacked directory to the PATH variable, as well as the location of the unpacked directory to SPARK_HOME.

We recommend installing either Spark 3.0.3, or Spark 3.1.2 (we also provide a RumbleDB jar for Spark 2 for legacy purposes, however it is not recommended to use it for new projects).

You can test that Spark was correctly installed with:

    spark-submit --version
    
Another important comment: if you use Spark 2.4.x, you need to make sure that you have Java 8 and that, if you have several versions installed, JAVA_HOME correctly points to Java 8. Spark 2.4.x only supports Java 8. Spark 3.0.2 is documented to work with both Java 8 and Java 11, even though we have not tried Java 11 yet. If there is an issue with the Java version, RumbleDB will inform you with an appropriate error message. You can check the version that is configured with:

    java -version


### Download RumbleDB

RumbleDB is just a download with no installation. In order to run RumbleDB, you simply need to download the .jar file from the [download page](https://github.com/RumbleDB/rumble/releases) and put it in a directory of your choice (for example, right besides your data). If you use Spark 3, you can use the default jar. If you use Spark 2, make sure to use the corresponding jar (for-spark-2) and to replace the jar name accordingly in all our instructions.

### Create some data set

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

## Running simple queries locally

In a shell, from the directory where the RumbleDB .jar lies, type, all on one line:

    spark-submit rumbledb-1.16.0.jar --shell yes
                 
The RumbleDB shell appears:

        ____                  __    __     ____  ____ 
       / __ \__  ______ ___  / /_  / /__  / __ \/ __ )
      / /_/ / / / / __ `__ \/ __ \/ / _ \/ / / / __  |  The distributed JSONiq engine
     / _, _/ /_/ / / / / / / /_/ / /  __/ /_/ / /_/ /   1.15.0 "Ivory Palm
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
     
     json-file("data.json")
     
json-file() reads its input in parallel, and thus will also work on your machine with MB or GB files (for TB files, a cluster will be preferable). You should specify a minimum number of partitions, here 10 (note that this is a bit ridiculous for our tiny example, but it is very relevant for larger files), as locally no parallelization will happen if you do not specify this number.

    for $i in json-file("data.json", 10)
    return $i

The above creates a very simple Spark job and executes it. More complex queries will create several Spark jobs. But you will not see anything of it: this is all done behind the scenes. If you are curious, you can go to [localhost:4040](http://localhost:4040) in your browser while your query is running (it will not be available once the job is complete) and look at what is going on behind the scenes.

Data can be filtered with the where clause. Again, below the hood, a Spark transformation will be used:

    for $i in json-file("data.json", 10)
    where $i.quantity gt 99
    return $i
    
RumbleDB also supports grouping and aggregation, like so:

    for $i in json-file("data.json", 10)
    let $quantity := $i.quantity
    group by $product := $i.product
    return { "product" : $product, "total-quantity" : sum($quantity) }
    

RumbleDB also supports ordering. Note that clauses (where, let, group by, order by) can appear in any order.
The only constraint is that the first clause should be a for or a let clause.

    for $i in json-file("data.json", 10)
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
- Using RumbleDB with Jupyter notebooks. For this, you can run RumbleDB as a server with a simple command, and get started by downloading the main JSONiq tutorial as a Jupyter notebook and just clicking your way through it. More details are found in the Jupyter notebook section of this documentation. Jupyter notebooks work both locally and on a cluster.
- Write JSONiq code, and share it on the Web, as others can import it from HTTP in just one line from within their queries (no package publication or installation required) or specify an HTTP URL as an input query to RumbleDB!


