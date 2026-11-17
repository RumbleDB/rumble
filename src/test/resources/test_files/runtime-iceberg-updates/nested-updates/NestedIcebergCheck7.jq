(:JIQS: ShouldRun; UpdateDim=[1,14]; Output="[ { "string" : "hello" } ]" :)
let $data := iceberg-table("tempIcebergTable")
return $data.object.object.object_array