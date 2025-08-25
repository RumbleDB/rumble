(:JIQS: ShouldRun; UpdateDim=[7,5]; Output="" :)
let $data := table("tempDeltaTable")
return rename json $data.object.object.object_array[[1]].new_ins as "success"