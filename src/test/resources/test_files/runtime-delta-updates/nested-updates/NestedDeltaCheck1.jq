(:JIQS: ShouldRun; UpdateDim=[1,2]; Output="SUCCESS" :)
let $data := delta-file("./tempDeltaTable")
return $data.object.object.object_array[[1]].new_ins