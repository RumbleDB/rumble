(:JIQS: ShouldRun; UpdateDim=[7,11]; Output="" :)
let $data := table("tempDeltaTable")
return replace value of json $data.object.object.object_array[[2]] with { "string" : "NEW DOUBLE SUCCESS" }