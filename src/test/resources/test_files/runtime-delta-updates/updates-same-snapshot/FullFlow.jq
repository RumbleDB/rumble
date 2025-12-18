(:JIQS: ShouldRun; UpdateDim=[9,9]; Output="({ "index" : 1000 }, { "index" : 10 })" :)
(insert { "index" : 10 } last into collection delta-file("./dfile"),
delete delta-file("./dfile")[1] from collection,
edit delta-file("./dfile")[1] into { "index" : 1000 } in collection);
delta-file("./dfile")

