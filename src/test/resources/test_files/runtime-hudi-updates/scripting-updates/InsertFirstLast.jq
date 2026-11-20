(:JIQS: ShouldRun; UpdateDim=[3,3]; Output="({ "v" : "first" }, { "v" : "last" })" :)
create collection hudi-table("hoodie3") with ();
insert { "v": "last" } last into collection hudi-table("hoodie3");
insert { "v": "first" } first into collection hudi-table("hoodie3");
hudi-table("hoodie3")