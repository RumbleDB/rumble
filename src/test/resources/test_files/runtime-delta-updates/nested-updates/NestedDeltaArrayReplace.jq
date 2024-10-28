(:JIQS: ShouldRun; UpdateDim=[1,11]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return replace value of json $data.object.object.object_array[[2]] with { "string" : "NEW DOUBLE SUCCESS" }