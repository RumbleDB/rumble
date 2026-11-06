(:JIQS: ShouldRun; UpdateDim=[1,9]; Output="" :)
let $data := iceberg-table("tempIcebergTable")
return insert json {"string" : "NEW SUCCESS" } into $data.object.object.object_array at position 2