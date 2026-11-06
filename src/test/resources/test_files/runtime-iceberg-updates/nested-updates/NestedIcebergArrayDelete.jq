(:JIQS: ShouldRun; UpdateDim=[1,13]; Output="" :)
let $data := iceberg-table("tempIcebergTable")
return delete json $data.object.object.object_array[[2]]