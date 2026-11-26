(:JIQS: ShouldRun; UpdateDim=[3,5]; Output="({ "id" : 1, "v" : "A" }, { "id" : 2, "v" : "B2" }, { "id" : 3, "v" : "C" })" :)
create collection hudi-table("hoodie5") with (
  { "id": 1, "v": "A" },
  { "id": 2, "v": "B" },
  { "id": 3, "v": "C" });
edit hudi-table("hoodie5")[2] into { "id": 2, "v": "B2" } in collection;
hudi-table("hoodie5")