(:JIQS: ShouldRun; UpdateDim=[1,8]; Output="null" :)
let $data := iceberg-table("tempIcebergTable")
return $data.object.object.object_array[[1]].success