(:JIQS: ShouldRun; UpdateDim=[0,13]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return delete json $data.string_array[[2]]