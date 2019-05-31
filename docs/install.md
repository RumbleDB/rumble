# Installing Rumble

We show here how to install Rumble from the github repository if you wish to do so (for example, to use the latest master). However, the easiest way to use Rumble is to simply download the already compiled .jar files.

## Requirements

The following software is required:

- [Java SE](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 8 (last tested on 1.8.0_121). The JDK is needed here, but the JRE will be enough when the packaged JAR is available.
- [Spark](https://spark.apache.org/), version 2.0.0 (for example)
- [Ant](http://www.ant.org/), version 1.10.1
- [ANTLR](http://www.ant.org/), version 4.7 (supplied in our repository, also 4.5.3)
- [Maven](https://maven.apache.org/) 3.5.0

Important: Java 9 is not supported by Spark. You specifically need Java 8. 

Important: the ANTLR version varies with the Spark version, because Spark is also shipped with an ANTLR runtime (example: Spark 2.2.0 is with ANTLR 4.5.3, Spark 2.3.0 with ANTLR 4.7). The ANTLR runtime MUST match the ANTLR generator used to generate the Rumble jar file.

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
    
    $ git clone git@github.com:Rumble/rumble.git
    
Go to the root of this repository:

    $ cd rumble
    
## Compile

### ANTLRv4

For convenience, we have included the ANTLRv4 files for 4.5.3 and 4.7 in the lib directory of the repository (see corresponding license).

From the root directory of the rumble local checkout, you first need to build the parser:

    $ ant -buildfile build_antlr_parser.xml generate-parser -Dantlr.jar=/lib/antlr-4.7-complete.jar
    
### Rumble

Once the ANTLR sources have been generated, you can compile the entire project like so:

    $ mvn clean compile assembly:single
    
After successful completion, you can check the `target` directory, which should contain the compiled classes as well as the JAR file `spark-rumble-1.0-jar-with-dependencies.jar`.
    
## Running locally

The most straightforward to test if the above steps were successful is to run the Rumble shell locally, like so:

    $ spark-submit --master local[2] --deploy-mode client target/spark-rumble-1.0-jar-with-dependencies.jar --shell yes

The Rumble shell should start:

    Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
       _____                  __                    ________
      / ___/____  ____ ______/ /___________  ____  /  _/ __ \
      \__ \/ __ \/ __ `/ ___/ //_/ ___/ __ \/ __ \ / // / / /
     ___/ / /_/ / /_/ / /  / ,< (__  ) /_/ / / / // // /_/ /
    /____/ .___/\__,_/_/  /_/|_/____/\____/_/ /_/___/\___\_\
    Master: local[2]
    Item Display Limit: 1000
    Output Path: -
    Log Path: -
    Query Path : -

    jiqs$
    
You can now start typing interactive queries. Queries can span over multiple lines. You need to press return 3 times to confirm.
    
    jiqs$ "Hello, world!"

This produces the following results (`>>>` show the extra, empty lines that appear on the first two presses of the return key).

    jiqs$ "Hello, world!"
    >>> 
    >>> 
    Hello, world
    
You can try a few more queries.
    
    jiqs$ 2 + 2
    >>> 
    >>> 
    4
    
    jiqs$ 1 to 10
    >>> 
    >>> 
    ( 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    
This is it. Rumble is step and ready to go locally. You can now move on to a JSONiq tutorial. A Rumble tutorial will also follow soon.

## Running on a cluster

You can also try to run the Rumble shell on a cluster if you have one available and configured -- this is done in the same way as any other `spark-submit` command:

    $ spark-submit --master yarn --deploy-mode client --num-executors 40 spark-rumble-1.0-jar-with-dependencies.jar
    
More details are provided in the rest of the documentation.
