(:JIQS: ShouldRun; Output="{ "b" : 1 }" :)
let $a := {"a" : 1}
return
    copy $je := $a
    modify rename $je.a as "b"
    return $je