(:JIQS: ShouldRun; UpdateDim=[10,4]; Output="({ "id" : "before" }, { "id" : "base" }, { "id" : "after" })" :)
create collection iceberg-table("icy4") with { "id": "base" };
insert { "id": "before" } before iceberg-table("icy4")[1] into collection;
insert { "id": "after" }  after  iceberg-table("icy4")[2] into collection;
iceberg-table("icy4")