# Installing Sparksoniq

We show here how to install Sparksoniq from the github repository.

When the alpha release is done, many steps here will become unnecessary as the .jar will be available for download.

## Requirements

The following software is required:

- [Java SE](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 8 (last tested on 1.8.0_121). The JDK is needed here, but the JRE will be enough when the packaged JAR is available.
- [Spark](https://spark.apache.org/) 2.1.* (last tested on 2.1.1)
- [ANTLRv4](http://www.antlr.org/), version 4.6
- [Maven](https://maven.apache.org/) 3.5.0

### Checking the requirements

Type the following commands to check that the necessary commands are available. If not, you may need to either install the software, or make sure that it is on the PATH.

    $ java -version
    
    $ mvn --version

    $ ant -version

    $ spark-submit --version

## Checkout

You first need to download the sparksoniq code to your local machine.

In the shell, go to the desired location:

    $ cd some_directory
    
Clone the github repository:
    
    $ git clone git@github.com:Sparksoniq/sparksoniq.git
    
Go to the root of this repository:

    $ cd sparksoniq
    
## Compile

### ANTLRv4

If you havent already installed ANTLRv4, you need to do so.

From the root directory of the sparksoniq local checkout, you first need to build the parser:

    $ ant -buildfile build_antlr_parser.xml generate-parser
    
If an error is displayed that antlr-4.6-complete.jar cannot be find, you can specify its location manually like so:
    
    $ ant -buildfile build_antlr_parser.xml generate-parser ---Dantlr.jar=/some/location/antlr-4.6-complete.jar
    
### Sparksoniq

Once the ANTLR sources have been generated, you can compile the entire project like so:

    $ mvn clean compile assembly:single
    
After successful completion, you can check the target directory, which should contain the compiled classes as well as the JAR file `jsoniq-spark-app-1.0-jar-with-dependencies.jar`.
    
## Running locally

The most straightforward to test if the above steps were successful is to run the Sparksoniq shell locally, like so:

    $ spark-submit --class sparksoniq.ShellStart --master local[2] --deploy-mode client target/jsoniq-spark-app-1.0-jar-with-dependencies.jar --master local[2] --result-size 1000

The Sparksoniq shell should start:

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
    
This is it. Sparksoniq is step and ready to go locally. You can now move on to a JSONiq tutorial. A Sparksoniq tutorial will also follow soon.

## Running on a cluster

You can also try to run the Sparksoniq shell on a cluster if you have one available and configured -- this is done in the same way as any other `spark-submit` command:

    $ spark-submit --class sparksoniq.ShellStart --master yarn-client --deploy-mode client --num-executors 40 jsoniq-spark-app-1.0-jar-with-dependencies.jar --master yarn-client --result-size 1000
