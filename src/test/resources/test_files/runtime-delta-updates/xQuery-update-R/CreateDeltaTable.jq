(:JIQS: ShouldRun; UpdateDim=[3,0]; CreateTable; UpdateTable="./src/test/resources/test_files/runtime-delta-updates/xQuery-update-R/R"; Output="" :)
let $data := parquet-file("../../../queries/xQuery_update_R_data/R.parquet")
return $data