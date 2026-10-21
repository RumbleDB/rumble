(:JIQS: ShouldRun; Output="(1, 3)" :)
let $data := annotate(parallelize(for $i in 1 to 100001 return { "v" : $i }), { "v" : "integer" })
let $head := head#1
let $slice := (subsequence#3)($data, ?, 3)
return ($head($data).v, count($slice(1)))
