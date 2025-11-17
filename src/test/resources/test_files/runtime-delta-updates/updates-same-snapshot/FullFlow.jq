(:JIQS: ShouldRun; UpdateDim=[9,8]; Output="({ "index" : 1000 }, { "index" : 10 })" :)
(insert { "index" : 10 } last into collection delta-file("./dfile"),
edit delta-file("./dfile")[1] into { "index" : 1000 } in collection,
delete delta-file("./dfile")[1] from collection);
delta-file("./dfile")