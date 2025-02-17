(:JIQS: ShouldRun; Output="(foo, foo, foo, foo, foo)" :)
for $i in structured-json-file("../../../queries/conf-ex.json")
let $g := "foo"
return $g