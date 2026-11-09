(:JIQS: ShouldRun; Output="(1, 3)" :)
declare type local:row as { "v" : "integer" };
(: Validation uses DataFrames when enabled and stays local when parallelism is disabled. :)
let $data := validate type local:row* {
    parallelize(for $i in 1 to 100001 return { "v" : $i })
}
let $head := head#1
let $slice := (subsequence#3)($data, ?, 3)
return ($head($data).v, count($slice(1)))
