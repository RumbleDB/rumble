(:JIQS: ShouldRun; UpdateDim=[10,3]; Output="({ "v" : "first" }, { "v" : "last" })" :)
create collection iceberg-table("icy3") with ();
insert { "v": "last" } last into collection iceberg-table("icy3");
insert { "v": "first" } first into collection iceberg-table("icy3");
iceberg-table("icy3")