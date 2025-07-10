(:JIQS: ShouldRun; UpdateDim=[4,0]; CreateTable; UpdateTable="./src/test/resources/test_files/runtime-delta-updates/check-delta-transform/tempDeltaTable"; Output="" :)
let $data := parquet-file("../../../queries/sample-json.snappy.parquet")
return $data