(:JIQS: ShouldRun; UpdateDim=[1,7]; Output="" :)
let $data := iceberg-table("tempIcebergTable")
return delete json $data.object.object.object_array[[1]].success