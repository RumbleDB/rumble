# Getting Started

## Prerequisites

### Installing Spark

Rumble requires a Spark installation on ideally Linux or Mac. The easiest is to install Spark with a package management system, such as brew (on macOS) or apt-get (on Ubuntu), etc. We recommend installing the latest Spark 2.4.5.

Another way is to do a manual installation (which is also relatively easy). Then, we recommend installing 2.4.5 from [this page](https://spark.apache.org/downloads.html).
Download the file and unzip it, which will create a directory called spark-2.4.5-bin-hadoop2.7. 

Move over this directory to, for example, /usr/local/bin

    mv spark-2.4.5-bin-hadoop2.7 /usr/local/bin

and add /usr/local/bin/spark-2.4.5-bin-hadoop2.7 to the PATH variable

    export PATH=/usr/local/bin/spark-2.4.5-bin-hadoop2.7/bin:$PATH

You can test that this worked with:

    spark-submit --version
    
Also, a very important comment: you need to make sure that you have Java 8 and that, if you have several versions installed, JAVA_HOME correctly points to Java 8. Spark only supported Java 8. If you see the error "An error has occurred: Unsupported class file major version 55" when you try to use Rumble, then your Java version is not Java 8. You can check the version that is configured with:

    java -version


### Installing Rumble

In order to run Rumble, you need to download the .jar file from the [download page](https://github.com/Sparksoniq/rumble/releases)
and put it in a directory of your choice. For Spark 2.4.5 we recommend the .jar file with ant 4.7 (not 4.5.3).

### Creating some data set

Create, in the same directory as Rumble, a file data.json and put the following content inside. This is a list of JSON objects in the JSON Lines format.

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

    spark-submit spark-rumble-1.5.jar --shell yes
                 
The Rumble shell appears:

    Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
        ____                  __    __   
       / __ \__  ______ ___  / /_  / /__ 
      / /_/ / / / / __ `__ \/ __ \/ / _ \
     / _, _/ /_/ / / / / / / /_/ / /  __/
    /_/ |_|\__,_/_/ /_/ /_/_.___/_/\___/ 
    Master: local[2]
    Item Display Limit: 100
    Output Path: -
    Log Path: -
    Query Path : -

    rumble$
    
You can now start typing simple queries like the following few examples. Press *three times* the return key to execute a query.
A warning about the ANTLR version may appear if you didn't pick the right jar file, but often you can just ignore it.

    "Hello, World"
 
     1 + 1
 
     (3 * 4) div 5
     
The following query should output the file created above:
     
     json-file("data.json")
     
The above queries do not actually use Spark. Spark is used when the I/O workload can be parallelized, which is the case with a FLWOR expression.
The simplest such query goes like so, here with 10 partitions:

    for $i in json-file("data.json", 10)
    return $i

The above creates a very simple Spark job with only a creation and an action.

Data can be filtered with the where clause. Below the hood, a Spark transformation will be used:

    for $i in json-file("data.json", 10)
    where $i.quantity gt 99
    return $i
    
Rumble also supports grouping and aggregation, like so:

    for $i in json-file("data.json", 10)
    let $quantity := $i.quantity
    group by $product := $i.product
    return { "product" : $product, "total-quantity" : sum($quantity) }
    

Rumble also supports ordering, like so. Note that clauses (where, let, group by, order by) can appear in any order.
The only constraint is that the first clause should be a for or a let clause.

    for $i in json-file("data.json", 10)
    let $quantity := $i.quantity
    group by $product := $i.product
    let $sum := sum($quantity)
    order by $sum descending
    return { "product" : $product, "total-quantity" : $sum }

Finally, Rumble can also parallelize local data, exactly like Sparks' parallelize() creation:

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

More details on FLWOR expressions can be found in the [JSONiq specification](http://www.jsoniq.org/docs/JSONiq/html-single/index.html#chapter-flwor).

