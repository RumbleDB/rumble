(:JIQS: ShouldRun; UpdateDim=[7,10]; Output="{ "string" : "NEW SUCCESS" }" :)
let $data := table("tempDeltaTable")
return $data.object.object.object_array[[2]]