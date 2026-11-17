(:JIQS: ShouldRun; UpdateDim=[10,6]; Output="({ "id" : 1 }, { "id" : 3 })" :)
create collection delta-file("./dfile6") with (
  { "id": 1 },
  { "id": 2 },
  { "id": 3 });
delete delta-file("./dfile6")[2] from collection;
delta-file("./dfile6")