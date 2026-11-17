(:JIQS: ShouldRun; UpdateDim=[1,3]; Output="" :)
let $data := iceberg-table("tempIcebergTable")
return replace value of json $data.object.object.object_array[[1]].new_ins with "DOUBLE SUCCESS"