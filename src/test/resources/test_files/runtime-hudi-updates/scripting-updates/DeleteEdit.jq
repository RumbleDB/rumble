(:JIQS: ShouldRun; UpdateDim=[3,7]; Output="({ "id" : "B" }, { "id" : "C_EDITED" })" :)
create collection hudi-table("hoodie7") with (
  { "id": "A" },
  { "id": "B" },
  { "id": "C" });
delete hudi-table("hoodie7")[1] from collection;
edit hudi-table("hoodie7")[2] into { "id": "C_EDITED" } in collection;
hudi-table("hoodie7")