# Getting Started

## Prerequisites

### Install Spark

Rumble requires a Spark installation on Linux, Mac or Windows. The easiest of all solutions is to install the all-encompassing and popular Anaconda Data Science software, which contains Python, Jupyter notebooks, Spark and so on. Users who love the command line can also install Spark with a package management system, such as brew (on macOS) or apt-get (on Ubuntu), etc, or for the most adventurous, then can even directly download it, unpack it, and add the Spark bin directory to the PATH variable.

We recommend installing either Spark 2.4.6, or Spark 3.0.0.

You can test that Spark was correctly installed with:

    spark-submit --version
    
Another important comment: if you use Spark 2.4.x, you need to make sure that you have Java 8 and that, if you have several versions installed, JAVA_HOME correctly points to Java 8. Spark 2.4.x only supports Java 8. Spark 3.0.0 is documented to work with both Java 8 and Java 11, even though we have not tried Java 11 yet. If there is an issue with the Java version, Rumble will inform you with an appropriate error message. You can check the version that is configured with:

    java -version


### Download Rumble

Rumble is just a download with no installation. In order to run Rumble, you need to download the .jar file from the [download page](https://github.com/Sparksoniq/rumble/releases) and put it in a directory of your choice. If you use Spark 3.0.0, make sure to use the corresponding jar and to replace the jar name accordingly in all our instructions.

### Create some data set

Create, in the same directory as Rumble to keep it simple, a file data.json and put the following content inside. This is a small list of JSON objects in the JSON Lines format.

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

In a shell, from the directory where the rumble .jar lies, type, all on one line:

    spark-submit spark-rumble-1.9.1.jar --shell yes
                 
The Rumble shell appears:

        ____                  __    __   
       / __ \__  ______ ___  / /_  / /__ 
      / /_/ / / / / __ `__ \/ __ \/ / _ \  The distributed JSONiq engine
     / _, _/ /_/ / / / / / / /_/ / /  __/  1.9.1 "Scots pine" beta
    /_/ |_|\__,_/_/ /_/ /_/_.___/_/\___/
    
    
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
     
json-file reads its input in parallel, and thus will also work on your machine with MB or GB files (for TB files, a cluster will be preferable). You can specify a minimum number of partitions, here 10 (note that this is a bit ridiculous for our tiny example, but it is very relevant for larger files).

    for $i in json-file("data.json", 10)
    return $i

The above creates a very simple Spark job and executes it. More complex queries will create several Spark jobs. But you will not see anything of it: this is all done behind the scenes.

Data can be filtered with the where clause. Again, below the hood, a Spark transformation will be used:

    for $i in json-file("data.json", 10)
    where $i.quantity gt 99
    return $i
    
Rumble also supports grouping and aggregation, like so:

    for $i in json-file("data.json", 10)
    let $quantity := $i.quantity
    group by $product := $i.product
    return { "product" : $product, "total-quantity" : sum($quantity) }
    

Rumble also supports ordering. Note that clauses (where, let, group by, order by) can appear in any order.
The only constraint is that the first clause should be a for or a let clause.

    for $i in json-file("data.json", 10)
    let $quantity := $i.quantity
    group by $product := $i.product
    let $sum := sum($quantity)
    order by $sum descending
    return { "product" : $product, "total-quantity" : $sum }

Finally, Rumble can also parallelize data provided within the query, exactly like Sparks' parallelize() creation:

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

- Learning JSONiq. More details can be found in the JSONiq section of this documentation and in the JSONiq specification and tutorials.
- Storing some data on S3, creating a Spark cluster on Amazon EMR (or Azure blob storage and Azure, etc), and querying the data with Rumble. More details are found in the cluster section of this documentation.
- Using Rumble with Jupyter notebooks. For this, you can run Rumble as a server with a simple command, and get started by downloading the main JSONiq tutorial as a Jupyter notebook and just clicking your way through it. More details are found in the Jupyter notebook section of this documentation.
- Write JSONiq code, and share it on the Web for others to import from within their queries (no package publication or installation required)!


