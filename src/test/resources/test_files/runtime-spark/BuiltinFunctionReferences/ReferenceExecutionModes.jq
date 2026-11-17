(:JIQS: ShouldRun; Output="(7, 1, 8)" :)
(: One reference can be called with local and distributed arguments without retaining a previous call's mode. :)
let $f := head#1
return ($f((7, 9)), $f(parallelize(1 to 100001)), $f((8, 10)))
