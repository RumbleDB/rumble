(:JIQS: ShouldRun; UpdateDim=[3,8]; Output="({ "id" : 1 }, { "id" : 3 })" :)
create collection hudi-table("hoodie8") with (
  { "id": 1 },
  { "id": 2 },
  { "id": 3 });
edit hudi-table("hoodie8")[2] into { "id": 2, "edited": true } in collection;
delete hudi-table("hoodie8")[2] from collection;
hudi-table("hoodie8")