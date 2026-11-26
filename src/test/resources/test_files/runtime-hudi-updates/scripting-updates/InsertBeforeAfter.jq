(:JIQS: ShouldRun; UpdateDim=[3,4]; Output="({ "id" : "before" }, { "id" : "base" }, { "id" : "after" })" :)
create collection hudi-table("hoodie4") with { "id": "base" };
insert { "id": "before" } before hudi-table("hoodie4")[1] into collection;
insert { "id": "after" }  after  hudi-table("hoodie4")[2] into collection;
hudi-table("hoodie4")