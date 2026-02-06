(:JIQS: ShouldRun; UpdateDim=[10,4]; Output="({ "id" : "before" }, { "id" : "base" }, { "id" : "after" })" :)
create collection delta-file("./dfile4") with { "id": "base" };
insert { "id": "before" } before delta-file("./dfile4")[1] into collection;
insert { "id": "after" }  after  delta-file("./dfile4")[2] into collection;
delta-file("./dfile4")