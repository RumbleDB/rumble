(:JIQS: ShouldRun; UpdateDim=[10,1]; Output="{ "x" : 1 }" :)
create collection iceberg-table("icy1") with { "x": 1 };
iceberg-table("icy1")