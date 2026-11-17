(:JIQS: ShouldRun; UpdateDim=[1,12]; Output="{ "string" : "NEW DOUBLE SUCCESS" }" :)
let $data := iceberg-table("tempIcebergTable")
return $data.object.object.object_array[[2]]