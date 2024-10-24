(:JIQS: ShouldRun; UpdateDim=[1,10]; Output="{ "string" : "NEW SUCCESS" }" :)
let $data := delta-file("./tempDeltaTable")
return $data.object.object.object_array[[2]]