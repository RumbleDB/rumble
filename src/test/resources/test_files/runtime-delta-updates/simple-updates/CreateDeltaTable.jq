(:JIQS: ShouldRun; UpdateDim=[0,0]; Output="" :)
create collection delta-file("tempDeltaTable") with parquet-file("../../../queries/sample-json.snappy.parquet");