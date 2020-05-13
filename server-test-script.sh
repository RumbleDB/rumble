# start server as a background process
spark-submit target/spark-rumble-1.6.0-jar-with-dependencies.jar --server yes --port 8000 &
sleep 5

# ensure server is ready to run while running the first test
iteration=0
while true
do
    result=$(curl --silent --show-error --stderr - --data "parallelize(1 to 10)[2]" -X GET "http://localhost:8000/jsoniq")
    if [[ $result = 'curl: (7) Failed to connect to localhost port 8000: Connection refused' ]];
    then
        iteration=$((iteration+1))
        echo "Waiting for server to come online: Trial $iteration"

        if [[ $iteration -ge 10 ]];
        then
            echo "The server cannot be reached."
            exit 1
        else
            sleep 3
            continue;
        fi
    else
        break
    fi
done

success_count=0
fail_count=0

### TEST 1 ###
result=$(curl --silent --show-error --stderr - --data "parallelize(1 to 10)[2]" -X GET "http://localhost:8000/jsoniq")
if [[ $result = '{ "values" : [ 2 ], "status" : 200 }' ]];
then
    echo 'Test 1: Success'
    success_count=$((success_count+1))
else
    echo 'Test 1: Fail'
    fail_count=$((fail_count+1))
fi

### TEST 2 ###
echo "1+1" > /tmp/query.jq
result=$(curl --silent --show-error --stderr - -X GET "http://localhost:8000/jsoniq?query-path=/tmp/query.jq") > /dev/null
if [[ $result = '{ "values" : [ 2 ], "status" : 200 }' ]]
then
    echo 'Test 2: Success'
    success_count=$((success_count+1))
else
    echo 'Test 2: Fail'
    fail_count=$((fail_count+1))
fi

### TEST 3 ###

rm -rf /tmp/output
result=$(curl --silent --show-error --stderr - -X GET "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output")
if [[ $result = '{ "error-message" : "Error [err: RBST0001 ] The POST method must be used if specifying an output path, as this has side effects.", "error-code" : "RBST0001", "stack-trace" : [ "org.rumbledb.server.RumbleHandler.handle(RumbleHandler.java:93)", "com.sun.net.httpserver.Filter$Chain.doFilter(Filter.java:79)", "sun.net.httpserver.AuthFilter.doFilter(AuthFilter.java:83)", "com.sun.net.httpserver.Filter$Chain.doFilter(Filter.java:82)", "sun.net.httpserver.ServerImpl$Exchange$LinkHandler.handle(ServerImpl.java:675)", "com.sun.net.httpserver.Filter$Chain.doFilter(Filter.java:79)", "sun.net.httpserver.ServerImpl$Exchange.run(ServerImpl.java:647)", "sun.net.httpserver.ServerImpl$DefaultExecutor.execute(ServerImpl.java:158)", "sun.net.httpserver.ServerImpl$Dispatcher.handle(ServerImpl.java:431)", "sun.net.httpserver.ServerImpl$Dispatcher.run(ServerImpl.java:396)", "java.lang.Thread.run(Thread.java:748)" ] }' ]]
then
    echo 'Test 3: Success'
    success_count=$((success_count+1))
else
    echo 'Test 3: Fail'
    fail_count=$((fail_count+1))
fi

### TEST 4 ###

echo "1+1" > /tmp/query.jq
rm -rf /tmp/output
result=$(curl --silent --show-error --stderr - -X POST "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output")
if [[ $result = '{ "values" : [ 2 ], "status" : 200, "output-path" : "\/tmp\/output" }' ]]
then
    echo 'Test 4: Success'
    success_count=$((success_count+1))
else
    echo 'Test 4: Fail'
    fail_count=$((fail_count+1))
fi


### TEST 5 ###
rm -rf /tmp/output
curl --silent --show-error --stderr - -X POST "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output" > /dev/null 
result=$(curl --silent --show-error --stderr - -X POST "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output")
if [[ $result = '{ "error-message" : "Unexpected error: Output path \/tmp\/output already exists. Please use --overwrite yes to overwrite. We should investigate this. Please contact us or file an issue on GitHub with your query.", "error-code" : "RBST0004", "stack-trace" : [ "org.rumbledb.cli.JsoniqQueryExecutor.checkOutputFile(JsoniqQueryExecutor.java:63)", "org.rumbledb.cli.JsoniqQueryExecutor.runQuery(JsoniqQueryExecutor.java:73)", "org.rumbledb.server.RumbleHandler.handle(RumbleHandler.java:97)", "com.sun.net.httpserver.Filter$Chain.doFilter(Filter.java:79)", "sun.net.httpserver.AuthFilter.doFilter(AuthFilter.java:83)", "com.sun.net.httpserver.Filter$Chain.doFilter(Filter.java:82)", "sun.net.httpserver.ServerImpl$Exchange$LinkHandler.handle(ServerImpl.java:675)", "com.sun.net.httpserver.Filter$Chain.doFilter(Filter.java:79)", "sun.net.httpserver.ServerImpl$Exchange.run(ServerImpl.java:647)", "sun.net.httpserver.ServerImpl$DefaultExecutor.execute(ServerImpl.java:158)", "sun.net.httpserver.ServerImpl$Dispatcher.handle(ServerImpl.java:431)", "sun.net.httpserver.ServerImpl$Dispatcher.run(ServerImpl.java:396)", "java.lang.Thread.run(Thread.java:748)" ] }' ]]
then
    echo 'Test 5: Success'
    success_count=$((success_count+1))
else
    echo 'Test 5: Fail'
    fail_count=$((fail_count+1))
fi

### TEST 6 ###
rm -rf /tmp/output
curl --silent --show-error --stderr - -X POST "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output" > /dev/null 
result=$(curl --silent --show-error --stderr - -X POST "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output&overwrite=yes")
if [[ $result = '{ "values" : [ 2 ], "status" : 200, "output-path" : "\/tmp\/output" }' ]]
then
    echo 'Test 6: Success'
    success_count=$((success_count+1))
else
    echo 'Test 6: Fail'
    fail_count=$((fail_count+1))
fi

echo "Succesful tests: $success_count, Failed tests: $fail_count."
if [[ $fail_count -gt 0 ]];
then
    exit 1
fi

# kill rumble server
pkill -f rumble
