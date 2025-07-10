(:JIQS: ShouldRun; Output="({ "foobar" : 3 }, { "foobar" : 9 }, { "foobar" : 3 }, { "foobar" : 9 }, { "foobar" : 3 }, { "foobar" : 9 }, { "foobar" : 3 }, { "foobar" : 9 }, { "foobar" : 3 }, { "foobar" : 9 }, { "foobar" : 3 }, { "foobar" : 9 }, { "foobar" : 3 }, { "foobar" : 9 })" :)
for $i in structured-json-lines("../../../queries/denormalized2.json")
let $c := $i.foo[[2]].bar[[1]]
return $c