(:JIQS: ShouldRun; UpdateDim=[2,0]; CreateTable; UpdateTable="./src/test/resources/test_files/runtime-delta-updates/multirow-updates/tempDeltaTable"; Output="" :)
let $data := parquet-file("../../../queries/multirow.parquet")
return $data