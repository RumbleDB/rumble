(:JIQS: ShouldRun; UpdateDim=[10,7]; Output="({ "id" : "B" }, { "id" : "C_EDITED" })" :)
create collection delta-file("./dfile7") with (
  { "id": "A" },
  { "id": "B" },
  { "id": "C" });
delete delta-file("./dfile7")[1] from collection;
edit delta-file("./dfile7")[2] into { "id": "C_EDITED" } in collection;
delta-file("./dfile7")