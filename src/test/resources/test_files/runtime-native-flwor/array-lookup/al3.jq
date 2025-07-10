(:JIQS: ShouldRun; Output="({ "bar" : 3, "foobar" : 4 }, { "bar" : 9, "foobar" : 10 }, { "bar" : 3, "foobar" : 4 }, { "bar" : 9, "foobar" : 10 }, { "bar" : 3, "foobar" : 4 }, { "bar" : 9, "foobar" : 10 }, { "bar" : 3, "foobar" : 4 }, { "bar" : 9, "foobar" : 10 }, { "bar" : 3, "foobar" : 4 }, { "bar" : 9, "foobar" : 10 }, { "bar" : 3, "foobar" : 4 }, { "bar" : 9, "foobar" : 10 }, { "bar" : 3, "foobar" : 4 }, { "bar" : 9, "foobar" : 10 })" :)
for $i in structured-json-lines("../../../queries/denormalized.json")
let $c := $i.foo[[2]]
return $c