(:JIQS: ShouldRun; UpdateDim=[1,12]; Output="{ "string" : "NEW DOUBLE SUCCESS" }" :)
let $data := delta-file("./tempDeltaTable")
return $data.object.object.object_array[[2]]