(:JIQS: ShouldRun; UpdateDim=[10,6]; Output="({ "id" : 1 }, { "id" : 3 })" :)
create collection iceberg-table("icy6") with (
  { "id": 1 },
  { "id": 2 },
  { "id": 3 });
delete iceberg-table("icy6")[2] from collection;
iceberg-table("icy6")