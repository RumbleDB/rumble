(:JIQS: ShouldRun; UpdateDim=[1,12]; Output="{ "string" : "NEW DOUBLE SUCCESS" }" :)
let $data := hudi-table("tempHudiTable")
return $data.object.object.object_array[[2]]