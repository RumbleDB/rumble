(:JIQS: ShouldRun; UpdateDim=[10,3]; Output="({ "v" : "first" }, { "v" : "last" })" :)
create collection delta-file("./dfile3") with ();
insert { "v": "last" } last into collection delta-file("./dfile3");
insert { "v": "first" } first into collection delta-file("./dfile3");
delta-file("./dfile3")