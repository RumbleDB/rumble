(:JIQS: ShouldRun; Output="100001" :)
declare type local:row as { "n" : "integer" };
let $f := function($min as integer, $rows as local:row*) as local:row* {
    for $row in $rows
    where $row.n gt $min
    return $row
}
let $data := validate type local:row* {
    parallelize(for $i in 1 to 100001 return { "n" : $i })
}
return count($f(0, $data))
