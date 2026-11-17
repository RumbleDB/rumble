(:JIQS: ShouldRun; UpdateDim=[1,11]; Output="" :)
let $data := iceberg-table("tempIcebergTable")
return replace value of json $data.object.object.object_array[[2]] with { "string" : "NEW DOUBLE SUCCESS" }