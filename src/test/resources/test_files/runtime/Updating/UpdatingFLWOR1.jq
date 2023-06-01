(:JIQS: ShouldRun; Output="{ "a" : { "b" : 1 } }" :)
copy $je := { "a" : {"foo" : 1}}
modify
    let $l := $je.a
    return
        rename $l.foo as "b"
return $je