(:JIQS: ShouldRun; UpdateDim=[1,10]; Output="{ "string" : "NEW SUCCESS" }" :)
let $data := hudi-table("tempHudiTable")
return $data.object.object.object_array[[2]]