(:JIQS: ShouldRun; UpdateDim=[10,8]; Output="({ "id" : 1 }, { "id" : 3 })" :)
create collection iceberg-table("icy8") with (
  { "id": 1 },
  { "id": 2 },
  { "id": 3 });
edit iceberg-table("icy8")[2] into { "id": 2, "edited": true } in collection;
delete iceberg-table("icy8")[2] from collection;
iceberg-table("icy8")