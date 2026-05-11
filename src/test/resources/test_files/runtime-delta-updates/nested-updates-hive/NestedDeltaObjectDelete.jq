(:JIQS: ShouldRun; UpdateDim=[7,7]; Output="" :)
let $data := table("tempDeltaTable")
return delete json $data.object.object.object_array[[1]].success