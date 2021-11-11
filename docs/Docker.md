# With docker

## Running simple queries with Docker

Docker is the easiest way to get a standard environment that just works.

You can download Docker from [here](https://www.docker.com/).

Then, in a shell, type, all on one line:

    docker run -i rumbledb/rumble --shell yes
                 
The first time, it might take some time to download everything, but this is all done automatically. When there are RumbleDB updates, this will also trigger a re-download. Otherwise, subsequent commands will run immediately.

The RumbleDB shell appears:

        ____                  __    __     ____  ____ 
       / __ \__  ______ ___  / /_  / /__  / __ \/ __ )
      / /_/ / / / / __ `__ \/ __ \/ / _ \/ / / / __  |  The distributed JSONiq engine
     / _, _/ /_/ / / / / / / /_/ / /  __/ /_/ / /_/ /   1.16.0 "Shagbark Hickory" beta
    /_/ |_|\__,_/_/ /_/ /_/_.___/_/\___/_____/_____/  

    
    App name: spark-rumble-jar-with-dependencies.jar
    Master: local[*]
    Driver's memory: (not set)
    Number of executors (only applies if running on a cluster): (not set)
    Cores per executor (only applies if running on a cluster): (not set)
    Memory per executor (only applies if running on a cluster): (not set)
    Dynamic allocation: (not set)
    Item Display Limit: 200
    Output Path: -
    Log Path: -
    Query Path : -

    RumbleDB$
    
You can now start typing simple queries like the following few examples. Press *three times* the return key to execute a query.

    "Hello, World"
    
or
 
     1 + 1
     
or
 
     (3 * 4) div 5
     
The above queries do not actually use Spark. Spark is used when the I/O workload can be parallelized. The following query should output the file created above.
     
     json-file("https://rumbledb.org/samples/products-small.json")
     
json-file() reads its input in parallel, and thus will also work on your machine with MB or GB files (for TB files, a cluster will be preferable). You should specify a minimum number of partitions, here 10 (note that this is a bit ridiculous for our tiny example, but it is very relevant for larger files), as locally no parallelization will happen if you do not specify this number.

    for $i in json-file("https://rumbledb.org/samples/products-small.json", 10)
    return $i

The above creates a very simple Spark job and executes it. More complex queries will create several Spark jobs. But you will not see anything of it: this is all done behind the scenes. If you are curious, you can go to [localhost:4040](http://localhost:4040) in your browser while your query is running (it will not be available once the job is complete) and look at what is going on behind the scenes.

Data can be filtered with the where clause. Again, below the hood, a Spark transformation will be used:

    for $i in json-file("https://rumbledb.org/samples/products-small.json", 10)
    where $i.quantity gt 99
    return $i
    
RumbleDB also supports grouping and aggregation, like so:

    for $i in json-file("https://rumbledb.org/samples/products-small.json", 10)
    let $quantity := $i.quantity
    group by $product := $i.product
    return { "product" : $product, "total-quantity" : sum($quantity) }
    

RumbleDB also supports ordering. Note that clauses (where, let, group by, order by) can appear in any order.
The only constraint is that the first clause should be a for or a let clause.

    for $i in json-file("https://rumbledb.org/samples/products-small.json", 10)
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

## Querying locak files with the docker version of RumbleDB

In order to query your local files, you need to mount a local directory to a directory within the docker. This is done with the `--mount` option, and the source path must be absolute. For the target, you can pick anything that makes sense to you.

    docker run -t -i --mount type=bind,source=/path/to/my/directory,target=/home rumbledb/rumble --shell yes
    
Then you can go ahead and use local paths in input functions, like so:

    for $i in json-file("/path/to/my/directory/products-small.json", 10)
    where $i.quantity gt 99
    return $i
