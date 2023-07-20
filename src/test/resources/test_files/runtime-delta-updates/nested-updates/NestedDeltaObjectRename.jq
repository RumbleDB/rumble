(:JIQS: ShouldRun; UpdateDim=[1,5]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return rename $data.object.object.object_array[[1]].new_ins as "success"