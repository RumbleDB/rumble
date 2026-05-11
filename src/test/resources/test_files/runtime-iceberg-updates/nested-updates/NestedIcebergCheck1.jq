(:JIQS: ShouldRun; UpdateDim=[1,2]; Output="SUCCESS" :)
let $data := iceberg-table("tempIcebergTable")
return $data.object.object.object_array[[1]].new_ins