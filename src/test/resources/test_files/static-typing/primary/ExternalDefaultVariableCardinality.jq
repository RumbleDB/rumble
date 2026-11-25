xquery version "3.1";
(:JIQS: ShouldRun; Output="2" :)
(: The test runner binds externalIntegerItems to (1, 2, 3, 4, 5).
   A singleton default must not restrict the cardinality of that external binding. :)
declare variable $externalIntegerItems external := 0;

let $v := $externalIntegerItems
return $v[2]
