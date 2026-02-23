(:JIQS: ShouldRun; UpdateDim=[10,5]; Output="({ "id" : 1, "v" : "A" }, { "id" : 2, "v" : "B2" }, { "id" : 3, "v" : "C" })" :)
create collection iceberg-table("icy5") with (
  { "id": 1, "v": "A" },
  { "id": 2, "v": "B" },
  { "id": 3, "v": "C" });
edit iceberg-table("icy5")[2] into { "id": 2, "v": "B2" } in collection;
iceberg-table("icy5")