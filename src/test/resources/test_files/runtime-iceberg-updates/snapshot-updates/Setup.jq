(:JIQS: ShouldRun; UpdateDim=[9,4]; Output="{ "index" : 1 }" :)
(create collection iceberg-table("icy") with { "index" : 1 });
iceberg-table("icy")