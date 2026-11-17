(:JIQS: ShouldRun; UpdateDim=[1,14]; Output="[ { "string" : "hello" } ]" :)
let $data := delta-file("./tempDeltaTable")
return $data.object.object.object_array