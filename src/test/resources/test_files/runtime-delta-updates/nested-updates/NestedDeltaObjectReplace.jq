(:JIQS: ShouldRun; UpdateDim=[1,3]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return replace value of json $data.object.object.object_array[[1]].new_ins with "DOUBLE SUCCESS"