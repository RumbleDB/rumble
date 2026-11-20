(:JIQS: ShouldRun; UpdateDim=[3,6]; Output="({ "id" : 1 }, { "id" : 3 })" :)
create collection hudi-table("hoodie6") with (
  { "id": 1 },
  { "id": 2 },
  { "id": 3 });
delete hudi-table("hoodie6")[2] from collection;
hudi-table("hoodie6")