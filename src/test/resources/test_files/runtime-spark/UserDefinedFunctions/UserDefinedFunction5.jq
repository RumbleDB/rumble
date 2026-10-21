(:JIQS: ShouldRun; Output="(2, 3, 4)" :)
declare function local:increment($values as integer*) as double* {
  parallelize($values) ! ($$ + 1)
};
local:increment((1, 2, 3))

(: A function returning an RDD, whose result is promoted from integer to double. :)
