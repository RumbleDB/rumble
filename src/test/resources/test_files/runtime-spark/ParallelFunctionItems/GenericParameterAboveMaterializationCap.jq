(:JIQS: ShouldRun; Output="100001" :)
declare type local:row as { "n" : "integer" };
let $f := function($rows as item*) as item* { $rows }
let $data := validate type local:row* {
    parallelize(for $i in 1 to 100001 return { "n" : $i })
}
return count($f($data))
