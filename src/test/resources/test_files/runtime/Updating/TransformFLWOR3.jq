(:JIQS: ShouldRun; Output="({ "a" : 1 }, { "b" : 1 })" :)
let $oldx := {"a" : 1}
return
    copy json $newx := $oldx
    modify rename json $newx.a as "b"
    return ($oldx, $newx)