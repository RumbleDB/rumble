(:JIQS: ShouldRun; UpdateDim=[1,2]; Output="SUCCESS" :)
let $data := hudi-table("tempHudiTable")
return $data.object.object.object_array[[1]].new_ins