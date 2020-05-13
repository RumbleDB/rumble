# HTTP Server and integration with host languages

## Starting the HTTP server

Rumble can be run as an HTTP server that listens for queries. In order to do so, you can use the --server and --port parameters:

    spark-submit spark-rumble-1.6.0.jar --server yes --port 8001
    
This command will not return until you force it to (Ctrl+C on Linux and Mac). This is because the server has to run permanently to listen to incoming requests.

Most users will not have to do anything beyond running the above command. For most of them, the next step would be to open a Jupyter notebook that connects to this server automatically.

## Testing that it works (not necessary for most end users)

The HTTP server is meant not to be used directly by end users, but instead to make it possible to integrate Rumble in other languages and environments, such as Python and Jupyter notebooks. 

To test that the the server running, you can try the following address in your browser, assuming you have a query stored locally at /tmp/query.jq. All queries have to go to the /jsoniq path.

    http://localhost:8001/jsoniq?query-path=/tmp/query.jq
    
The request returns a JSON object, and the resulting sequence of items is in the values array.

    { "values" : [ "foo", "bar" ] }

Almost all parameters from the command line are exposed as HTTP parameters.

A query can also be submitted in the request body:

    curl -X POST --data '1+1' http://localhost:8001/jsoniq
    
## Use with Jupyter notebooks

With the HTTP server running, if you have installed PySpark and Jupyter notebooks (for example with the Anaconda data science package that does all of it automatically), you can create a Rumble magic by just executing the following code in a cell:

    import requests
    import json
    from IPython.core.magic import register_line_magic

    server='http://localhost:8001/jsoniq'

    @register_line_magic
    def rumble(query):
        return json.loads(requests.post(server, data=query).text)['values']
        
Where, of course, you need to adapt the port (8001) to the one you picked previously.

Then, you can execute queries in subsequent cells with

    %rumble 1 + 1
