# Installing RumbleDB

We show here how to install RumbleDB from the github repository if you wish to do so (for example, to use the latest master). However, the easiest way to use RumbleDB is to simply download the already compiled .jar files.

## Requirements

The following software is required:

- [Java SE](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 8 (last tested on OpenJDK 8u251). The version of Java is important, as Spark only works with Java 8 (and Scala 11 as of Spark 2.4.5).
- [Spark](https://spark.apache.org/), version 2.4.5 (for example)
- [Ant](http://www.ant.org/), version 1.11.1
- [ANTLR](http://www.ant.org/), version 4.7 (supplied in our repository)
- [Maven](https://maven.apache.org/) 3.6.0

Important: the ANTLR version varies with the Spark version, because Spark is also shipped with an ANTLR runtime (example: Spark 2.2.0 is with ANTLR 4.5.3, Spark 2.3.0 with ANTLR 4.7). The ANTLR runtime MUST match the ANTLR generator used to generate the RumbleDB jar file.

### Checking the requirements

Type the following commands to check that the necessary commands are available. If not, you may need to either install the software, or make sure that it is on the PATH.

    $ java -version
    
    $ mvn --version

    $ ant -version

    $ spark-submit --version

## Checkout

You first need to download the rumble code to your local machine.

In the shell, go to the desired location:

    $ cd some_directory
    
Clone the github repository:
    
    $ git clone https://github.com/RumbleDB/rumble.git
    
Go to the root of this repository:

    $ cd rumble
    
## Compile

### ANTLRv4

For convenience, we have included the ANTLRv4 files for 4.5.3 and 4.7 in the lib directory of the repository (see corresponding license). In the most recent and supported Spark versions, 4.7 is the correct ANTLR version to use.

From the root directory of the rumble local checkout, you first need to build the parser:

    $ ant -buildfile build_antlr_parser.xml generate-parser -Dantlr.jar=lib/antlr-4.7-complete.jar
    
### RumbleDB

Once the ANTLR sources have been generated, you can compile the entire project like so:

    $ mvn clean compile assembly:single
    
After successful completion, you can check the `target` directory, which should contain the compiled classes as well as the JAR file `spark-rumble-1.62.jar`.
    
## Running locally

The most straightforward to test if the above steps were successful is to run the RumbleDB shell locally, like so:

    $ spark-submit target/rumbledb-1.16.0.jar --shell yes

The RumbleDB shell should start:

    Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties

        ____                  __    __     ____  ____ 
       / __ \__  ______ ___  / /_  / /__  / __ \/ __ )
      / /_/ / / / / __ `__ \/ __ \/ / _ \/ / / / __  |  The distributed JSONiq engine
     / _, _/ /_/ / / / / / / /_/ / /  __/ /_/ / /_/ /   1.15.0 "Ivory Palm" beta
    /_/ |_|\__,_/_/ /_/ /_/_.___/_/\___/_____/_____/  

    Master: local[2]
    Item Display Limit: 1000
    Output Path: -
    Log Path: -
    Query Path : -

    rumble$
    
You can now start typing interactive queries. Queries can span over multiple lines. You need to press return 3 times to confirm.
    
    rumble$ "Hello, world!"

This produces the following results (`>>>` show the extra, empty lines that appear on the first two presses of the return key).

    rumble$ "Hello, world!"
    >>> 
    >>> 
    Hello, world
    
You can try a few more queries.
    
    rumble$ 2 + 2
    >>> 
    >>> 
    4
    
    rumble$ 1 to 10
    >>> 
    >>> 
    ( 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    
This is it. RumbleDB is step and ready to go locally. You can now move on to a JSONiq tutorial. A RumbleDB tutorial will also follow soon.

## Running on a cluster

You can also try to run the RumbleDB shell on a cluster if you have one available and configured -- this is done with the same command, as the master and deployment mode are usually already set up in cloud-managed clusters. More details are provided in the rest of the documentation.
