(:JIQS: ShouldRun; UpdateDim=[0,11]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return replace value of json $data.string_array[[2]] with "DOUBLE SUCCESS"