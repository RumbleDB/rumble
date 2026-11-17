(:JIQS: ShouldRun; UpdateDim=[1,8]; Output="null" :)
let $data := delta-file("./tempDeltaTable")
return $data.object.object.object_array[[1]].success