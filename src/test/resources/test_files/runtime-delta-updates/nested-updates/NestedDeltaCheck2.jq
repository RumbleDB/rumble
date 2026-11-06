(:JIQS: ShouldRun; UpdateDim=[1,4]; Output="DOUBLE SUCCESS" :)
let $data := delta-file("./tempDeltaTable")
return $data.object.object.object_array[[1]].new_ins