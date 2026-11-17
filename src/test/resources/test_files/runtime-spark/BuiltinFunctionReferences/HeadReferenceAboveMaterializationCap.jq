(:JIQS: ShouldRun; Output="1" :)
(: The result fits below the materialization cap even though the input does not. :)
let $f := head#1
return $f(parallelize(1 to 100001))
