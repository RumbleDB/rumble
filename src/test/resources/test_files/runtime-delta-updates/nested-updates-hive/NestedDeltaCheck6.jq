(:JIQS: ShouldRun; UpdateDim=[7,12]; Output="{ "string" : "NEW DOUBLE SUCCESS" }" :)
let $data := table("tempDeltaTable")
return $data.object.object.object_array[[2]]