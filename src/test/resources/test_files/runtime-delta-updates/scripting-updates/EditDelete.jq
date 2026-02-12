(:JIQS: ShouldRun; UpdateDim=[10,8]; Output="({ "id" : 1 }, { "id" : 3 })" :)
create collection delta-file("./dfile8") with (
  { "id": 1 },
  { "id": 2 },
  { "id": 3 });
edit delta-file("./dfile8")[2] into { "id": 2, "edited": true } in collection;
delete delta-file("./dfile8")[2] from collection;
delta-file("./dfile8")