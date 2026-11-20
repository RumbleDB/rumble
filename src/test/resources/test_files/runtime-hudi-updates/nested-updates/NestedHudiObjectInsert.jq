(:JIQS: ShouldRun; UpdateDim=[1,1]; Output="" :)
let $data := hudi-table("tempHudiTable")
return insert json "new_ins" : "SUCCESS" into $data.object.object.object_array[[1]]