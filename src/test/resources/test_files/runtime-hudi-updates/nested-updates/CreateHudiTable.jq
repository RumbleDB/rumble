(:JIQS: ShouldRun; UpdateDim=[1,0]; Output="" :)
create collection hudi-table("tempHudiTable") with parquet-file("../../../queries/nested_parquet");
