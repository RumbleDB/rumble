(:JIQS: ShouldRun; Output="{ "b" : 1 }" :)
let $a := {"a" : 1}
return
    copy $je := $a
    modify rename json $je.a as "b"
    return $je