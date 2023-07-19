(:JIQS: ShouldRun; UpdateDim=[0,0]; UpdateTable="./src/test/resources/test_files/runtime-delta-updates/simple-updates/tempDeltaTable"; Output="" :)
let $data := parquet-file("../../../queries/sample-json.snappy.parquet")
return $data