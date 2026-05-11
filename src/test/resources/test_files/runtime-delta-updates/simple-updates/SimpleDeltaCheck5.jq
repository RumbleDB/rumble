(:JIQS: ShouldRun; UpdateDim=[0,10]; Output="SUCCESS" :)
let $data := delta-file("./tempDeltaTable")
return $data.string_array[[2]]