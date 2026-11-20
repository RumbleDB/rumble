(:JIQS: ShouldRun; UpdateDim=[1,13]; Output="" :)
let $data := hudi-table("tempHudiTable")
return delete json $data.object.object.object_array[[2]]