(:JIQS: ShouldRun; UpdateDim=[9,5]; Output="({ "should-be-first" : 9789 }, { "index" : 1 }, { "latest" : "laster" })" :)
(insert {"should-be-first": 9789} first into collection delta-file("./dfile"),
insert {"latest": "laster"} last into collection delta-file("./dfile"));
delta-file("./dfile")
