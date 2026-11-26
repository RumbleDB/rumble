(:JIQS: ShouldRun; UpdateDim=[1,5]; Output="" :)
let $data := hudi-table("tempHudiTable")
return rename json $data.object.object.object_array[[1]].new_ins as "success"