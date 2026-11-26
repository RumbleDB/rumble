(:JIQS: ShouldRun; UpdateDim=[1,6]; Output="DOUBLE SUCCESS" :)
let $data := hudi-table("tempHudiTable")
return $data.object.object.object_array[[1]].success