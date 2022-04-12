# With docker

## Known issue

On occasion, the docker version of RumbleDB used to throw a Kryo NoSuchMethodError on some systems. This should be fixed with version 1.18, let us know if this is not the case.

You can upgrade to the newest version with

    docker pull rumbledb/rumble

## Running simple queries with Docker

Docker is the easiest way to get a standard environment that just works.

You can download Docker from [here](https://www.docker.com/).

Then, in a shell, type, all on one line:

    docker run -i rumbledb/rumble repl
                 
The first time, it might take some time to download everything, but this is all done automatically. Subsequent commands will run immediately.

When there are new RumbleDB versions, you can upgrade with:

    docker pull rumbledb/rumble

The RumbleDB shell appears:

        ____                  __    __     ____  ____ 
       / __ \__  ______ ___  / /_  / /__  / __ \/ __ )
      / /_/ / / / / __ `__ \/ __ \/ / _ \/ / / / __  |  The distributed JSONiq engine
     / _, _/ /_/ / / / / / / /_/ / /  __/ /_/ / /_/ /   1.18.0 "Cacao Tree" beta
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

## Running the RumbleDB docker as a server

You can also run the docker as a server like so:

```
docker run -p 8001:8001 --rm rumbledb/rumble serve -p 8001 -h 0.0.0.0
```

You can change the port to something else than 8001 at all three places it appears. Do not forget `-p 8001:8001` that forwards the port to the outside of the docker. Then, you can use a [jupyter notebook](https://github.com/RumbleDB/rumble/blob/master/RumbleSandbox.ipynb) connected to the RumbleDB docker server to write queries in it. Point the notebook to `http://localhost:8001/jsoniq` in the appropriate cell (or any other port).

## Querying local files with the docker version of RumbleDB

In order to query your local files, you need to mount a local directory to a directory within the docker. This is done with the `--mount` option, and the source path must be absolute. For the target, you can pick anything that makes sense to you.

For example, imagine you have a file products-small.json in the directory /path/to/my/directory. Then you need to run RumbleDB with:

    docker run -t -i --mount type=bind,source=/path/to/my/directory,target=/home rumbledb/rumble repl
    
Then you can go ahead and use absolute paths in the target directory in input functions, like so:

    for $i in json-file("/home/products-small.json", 10)
    where $i.quantity gt 99
    return $i

You can also mount a local directory in this way running it as a server rather than a shell.
