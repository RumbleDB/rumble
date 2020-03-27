(:JIQS: ShouldRun; Output="({ "str" : "asdffwe" }, { "str" : "cd" })" :)
for $i in (
{"str": 1},
{"str": 2},
{"str":  3.5},
{"str":  "asdffwe"},
{"str":  "cd"}
)
where typeswitch($i.str)
    case integer | decimal return false
    default return true
return $i
