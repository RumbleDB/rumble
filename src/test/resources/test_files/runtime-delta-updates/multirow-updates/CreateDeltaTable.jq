(:JIQS: ShouldRun; UpdateDim=[2,0]; Output="" :)
create collection delta-file("tempDeltaTable") with  parquet-file("../../../queries/multirow.parquet");