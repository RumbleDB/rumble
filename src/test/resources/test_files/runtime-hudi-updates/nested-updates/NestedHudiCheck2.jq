(:JIQS: ShouldRun; UpdateDim=[1,4]; Output="DOUBLE SUCCESS" :)
let $data := hudi-table("tempHudiTable")
return $data.object.object.object_array[[1]].new_ins