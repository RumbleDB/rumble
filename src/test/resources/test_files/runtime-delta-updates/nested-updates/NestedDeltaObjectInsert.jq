(:JIQS: ShouldRun; UpdateDim=[1,1]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return insert "new_ins" : "SUCCESS" into $data.object.object.object_array[[1]]