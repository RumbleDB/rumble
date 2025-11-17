(:JIQS: ShouldRun; UpdateDim=[9,2]; Output="({ "FIRST" : 0.1 }, { "BEFORE" : "works" }, { "AFTER" : "well" }, { "LAST" : 10 })" :)
(insert { "LAST" : 10 } last into collection delta-file("./dfile-empty"),
insert { "FIRST" : 0.1 } first into collection delta-file("./dfile-empty"),
insert { "BEFORE" : "works" } before delta-file("./dfile-empty")[1] into collection,
insert { "AFTER" : "well" } after delta-file("./dfile-empty")[1] into collection);
delta-file("./dfile-empty")