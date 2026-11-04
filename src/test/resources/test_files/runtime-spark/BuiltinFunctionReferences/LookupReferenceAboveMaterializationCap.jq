(:JIQS: ShouldRun; Output="1" :)
let $f := function-lookup(fn:QName("http://www.w3.org/2005/xpath-functions", "head"), 1)
return $f(parallelize(1 to 100001))
