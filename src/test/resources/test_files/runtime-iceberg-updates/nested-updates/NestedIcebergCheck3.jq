(:JIQS: ShouldRun; UpdateDim=[1,6]; Output="DOUBLE SUCCESS" :)
let $data := iceberg-table("tempIcebergTable")
return $data.object.object.object_array[[1]].success