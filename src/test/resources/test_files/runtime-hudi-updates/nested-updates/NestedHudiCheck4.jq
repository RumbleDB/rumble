(:JIQS: ShouldRun; UpdateDim=[1,8]; Output="null" :)
let $data := hudi-table("tempHudiTable")
return $data.object.object.object_array[[1]].success