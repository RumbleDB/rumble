(:JIQS: ShouldRun; UpdateDim=[7,13]; Output="" :)
let $data := table("tempDeltaTable")
return delete json $data.object.object.object_array[[2]]