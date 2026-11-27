(:JIQS: ShouldRun; Output="100001" :)
(: The Spark runners cap materialization at 100000; both the argument and result exceed it. :)
let $f := function($rows as integer*) as integer* { $rows }
return count($f(parallelize(1 to 100001)))
