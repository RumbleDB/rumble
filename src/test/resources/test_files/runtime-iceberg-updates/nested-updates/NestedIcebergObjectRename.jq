(:JIQS: ShouldRun; UpdateDim=[1,5]; Output="" :)
let $data := iceberg-table("tempIcebergTable")
return rename json $data.object.object.object_array[[1]].new_ins as "success"