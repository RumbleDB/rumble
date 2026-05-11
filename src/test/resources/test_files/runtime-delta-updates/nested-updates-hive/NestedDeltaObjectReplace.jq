(:JIQS: ShouldRun; UpdateDim=[7,3]; Output="" :)
let $data := table("tempDeltaTable")
return replace value of json $data.object.object.object_array[[1]].new_ins with "DOUBLE SUCCESS"