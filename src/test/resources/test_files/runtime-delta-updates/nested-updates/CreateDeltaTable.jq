(:JIQS: ShouldRun; UpdateDim=[1,0]; UpdateTable="./src/test/resources/test_files/runtime-delta-updates/nested-updates/tempDeltaTable"; Output="" :)
let $data := parquet-file("../../../queries/nested_parquet")
return $data