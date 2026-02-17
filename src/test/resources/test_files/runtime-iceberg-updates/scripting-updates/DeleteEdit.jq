(:JIQS: ShouldRun; UpdateDim=[10,7]; Output="({ "id" : "B" }, { "id" : "C_EDITED" })" :)
create collection iceberg-table("icy7") with (
  { "id": "A" },
  { "id": "B" },
  { "id": "C" });
delete iceberg-table("icy7")[1] from collection;
edit iceberg-table("icy7")[2] into { "id": "C_EDITED" } in collection;
iceberg-table("icy7")