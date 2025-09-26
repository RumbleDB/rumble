(:JIQS: ShouldRun; UpdateDim=[1,0]; Output="" :)
create collection delta-file("tempDeltaTable") with  parquet-file("../../../queries/nested_parquet");