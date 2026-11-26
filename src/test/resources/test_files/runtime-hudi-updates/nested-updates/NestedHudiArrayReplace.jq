(:JIQS: ShouldRun; UpdateDim=[1,11]; Output="" :)
let $data := hudi-table("tempHudiTable")
return replace value of json $data.object.object.object_array[[2]] with { "string" : "NEW DOUBLE SUCCESS" }