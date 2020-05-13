# start server as a background process
spark-submit target/spark-rumble-1.6.0-jar-with-dependencies.jar --server yes --port 8000 &
echo "Starting the Rumble server"
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
expected_result='{ "values" : [ 2 ] }' 
if [[ "$result" = "$expected_result" ]];
then
    echo 'Test 1: Success'
    success_count=$((success_count+1))
else
    echo "Test 1: Fail. Expected: $expected_result - Actual: $result"
    fail_count=$((fail_count+1))
fi

### TEST 2 ###
echo "1+1" > /tmp/query.jq
result=$(curl --silent --show-error --stderr - -X GET "http://localhost:8000/jsoniq?query-path=/tmp/query.jq") > /dev/null
expected_result='{ "values" : [ 2 ] }'
if [[ "$result" = "$expected_result" ]]
then
    echo 'Test 2: Success'
    success_count=$((success_count+1))
else
    echo "Test 2: Fail. Expected: $expected_result - Actual: $result"
    fail_count=$((fail_count+1))
fi

### TEST 3 ###

rm -rf /tmp/output
result=$(curl --silent --show-error --stderr - -X GET "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output")
expected_result='The POST method must be used if specifying an output path, as this has side effects.'
if [[ "$result" = "$expected_result" ]]
then
    echo 'Test 3: Success'
    success_count=$((success_count+1))
else
    echo "Test 3: Fail. Expected: $expected_result - Actual: $result"
    fail_count=$((fail_count+1))
fi

### TEST 4 ###
sleep 1 # Mystery: output path causes errors without a sleep statement
rm -rf /tmp/output
echo "1+1" > /tmp/query.jq
result=$(curl --silent --show-error --stderr - -X POST "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output")
expected_result='{ "output-path" : "\/tmp\/output" }'
file_output=$( cat /tmp/output )
expected_file_output=2
if [[ "$result" = "$expected_result" && "$file_output" = "$expected_file_output" ]]
then
    echo 'Test 4: Success'
    success_count=$((success_count+1))
else
    echo "Test 4: Fail. Expected: $expected_result - Actual: $result. Expected file output: $expected_file_output - Actual file output: $file_output"
    fail_count=$((fail_count+1))
fi


### TEST 5 ###
rm -rf /tmp/output
curl --silent --show-error --stderr - -X POST "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output" > /dev/null 
result=$(curl --silent --show-error --stderr - -X POST "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output")
expected_result='{ "error-message" : "Error [err: RBST0001 ] Output path \/tmp\/output already exists. Please use --overwrite yes to overwrite.", "error-code" : "RBST0001", "stack-trace" : [ "org.rumbledb.cli.JsoniqQueryExecutor.checkOutputFile(JsoniqQueryExecutor.java:64)", "org.rumbledb.cli.JsoniqQueryExecutor.runQuery(JsoniqQueryExecutor.java:74)", "org.rumbledb.server.RumbleHttpHandler.handle(RumbleHttpHandler.java:95)", "com.sun.net.httpserver.Filter$Chain.doFilter(Filter.java:79)", "sun.net.httpserver.AuthFilter.doFilter(AuthFilter.java:83)", "com.sun.net.httpserver.Filter$Chain.doFilter(Filter.java:82)", "sun.net.httpserver.ServerImpl$Exchange$LinkHandler.handle(ServerImpl.java:675)", "com.sun.net.httpserver.Filter$Chain.doFilter(Filter.java:79)", "sun.net.httpserver.ServerImpl$Exchange.run(ServerImpl.java:647)", "sun.net.httpserver.ServerImpl$DefaultExecutor.execute(ServerImpl.java:158)", "sun.net.httpserver.ServerImpl$Dispatcher.handle(ServerImpl.java:431)", "sun.net.httpserver.ServerImpl$Dispatcher.run(ServerImpl.java:396)", "java.lang.Thread.run(Thread.java:748)" ] }'
if [[ "$result" = "$expected_result" ]]
then
    echo 'Test 5: Success'
    success_count=$((success_count+1))
else
    echo "Test 5: Fail. Expected: $expected_result - Actual: $result. Expected file output: $expected_file_output - Actual file output: $file_output"
    fail_count=$((fail_count+1))
fi

### TEST 6 ###
rm -rf /tmp/output
curl --silent --show-error --stderr - -X POST "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output" > /dev/null 
result=$(curl --silent --show-error --stderr - -X POST "http://localhost:8000/jsoniq?query-path=/tmp/query.jq&output-path=/tmp/output&overwrite=yes")
expected_result='{ "output-path" : "\/tmp\/output" }'
file_output=$( cat /tmp/output )
expected_file_output=2
if [[ "$result" = "$expected_result" && "$file_output" = "$expected_file_output" ]]
then
    echo 'Test 6: Success'
    success_count=$((success_count+1))
else
    echo "Test 6: Fail. Expected: $expected_result - Actual: $result. Expected file output: $expected_file_output - Actual file output: $file_output"
    fail_count=$((fail_count+1))
fi

# kill rumble server process
echo "Stopping the Rumble server"
pkill -f rumble

echo "Succesful tests: $success_count, Failed tests: $fail_count."
if [[ $fail_count -gt 0 ]];
then
    exit 1
fi

