(:JIQS: ShouldRun; UpdateDim=[7,1]; Output="" :)
let $data := table("tempDeltaTable")
return insert json "new_ins" : "SUCCESS" into $data.object.object.object_array[[1]]