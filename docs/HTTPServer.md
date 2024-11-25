# HTTP Server and integration with host languages

## Starting the HTTP server

RumbleDB can be run as an HTTP server that listens for queries. In order to do so, you can use the --server and --port parameters:

    spark-submit rumbledb-1.22.0.jar serve -p 8001
    
This command will not return until you force it to (Ctrl+C on Linux and Mac). This is because the server has to run permanently to listen to incoming requests.

Most users will not have to do anything beyond running the above command. For most of them, the next step would be to open a Jupyter notebook that connects to this server automatically.

This HTTP server is built as a basic server for the single user use case, i.e., the user runs their own RumbleDB server on their laptop or cluster, and connects to it via their Jupyter notebook, one query at a time. Some of our users have more advanced needs, or have a larger user base, and typically prefer to implement their own HTTP server, lauching RumbleDB queries either via the public RumbleDB Java API (like the basic HTTP server does -- so its code can serve as a demo of the Java API) or via the RumbleDB CLI.

Caution! Launching a server always has consequences on security, especially as RumbleDB can read from and write to your disk; So make sure you activate your firewall. In later versions, we may support authentication tokens.

## Testing that it works (not necessary for most end users)

The HTTP server is meant not to be used directly by end users, but instead to make it possible to integrate RumbleDB in other languages and environments, such as Python and Jupyter notebooks. 

To test that the server is running, you can try the following address in your browser, assuming you have a query stored locally at /tmp/query.jq. All queries have to go to the /jsoniq path.

    http://localhost:8001/jsoniq?query-path=/tmp/query.jq
    
The request returns a JSON object, and the resulting sequence of items is in the values array.

    { "values" : [ "foo", "bar" ] }

Almost all parameters from the command line are exposed as HTTP parameters.

A query can also be submitted in the request body:

    curl -X POST --data '1+1' http://localhost:8001/jsoniq
    
## Use with Jupyter notebooks

With the HTTP server running, if you have installed Python and Jupyter notebooks (for example with the Anaconda data science package that does all of it automatically), you can create a RumbleDB magic by just executing the following code in a cell:

    !pip install rumbledb
    %load_ext rumbledb
    %env RUMBLEDB_SERVER=http://localhost:8001/jsoniq

Where, of course, you need to adapt the port (8001) to the one you picked previously.

Then, you can execute queries in subsequent cells with:

    %jsoniq 1 + 1

or on multiple lines:

    %%jsoniq
    for $doc in json-lines("my-file")
    where $doc.foo eq "bar"
    return $doc

    
## Use with clusters

You can also let RumbleDB run as an HTTP server on the master node of a cluster, e.g. on Amazon EMR or Azure. You just need to:

- Create the cluster (it is usually just the push of a few buttons in Amazon or Azure)
- Wait for a few minutes
- Make sure that your own IP has incoming access to EMR machines by configuring the security group properly. You usually only need to do so the first time you set up a cluster (if your IP address remains the same), because the security group configuration will be reused for future EMR clusters.

Then there are two options

### With SSH tunneling

- Connect to the master with SSH with an extra parameter for securely tunneling the HTTP connection (for example `-L 8001:localhost:8001` or any port of your choosing)
- Download the RumbleDB jar to the master node

    wget https://github.com/RumbleDB/rumble/releases/download/v1.22.0/rumbledb-1.22.0.jar
    
- Launch the HTTP server on the master node (it will be accessible under `http://localhost:8001/jsoniq`).

    spark-submit rumbledb-1.22.0.jar serve -p 8001

- And then use Jupyter notebooks in the same way you would do it locally (it magically works because of the tunneling)

### With the EC2 hostname

There is also another way that does not need any tunnelling: you can specify the hostname of your EC2 machine (copied over from the EC2 dashboard) with the --host parameter. For example, with the placeholder <ec2-hostname>:

    spark-submit rumbledb-1.22.0.jar serve -p 8001 -h <ec2-hostname>

You also need to make sure in your EMR security group that the chosen port (e.g., 8001) is accessible from the machine in which you run your Jupyter notebook. Then, you can point your Jupyter notebook on this machine to `http://<ec2-hostname>:8001/jsoniq`.

Be careful not to open this port to the whole world, as queries can be sent that read and write to the EC2 machine and anything it has access to (like S3).
