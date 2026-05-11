(:JIQS: ShouldRun; UpdateDim=[1,13]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return delete json $data.object.object.object_array[[2]]