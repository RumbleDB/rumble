(:JIQS: ShouldRun; UpdateDim=[9,5]; Output="10" :)
(insert { "index" : 1 } first into collection delta-file("./dfile"),
insert { "index" : 2 } first into collection delta-file("./dfile"),
insert { "index" : 3 } first into collection delta-file("./dfile"),
insert { "index" : 4 } first into collection delta-file("./dfile"),
insert { "index" : 5 } first into collection delta-file("./dfile"),
insert { "index" : 6 } first into collection delta-file("./dfile"),
insert { "index" : 7 } first into collection delta-file("./dfile"),
insert { "index" : 8 } first into collection delta-file("./dfile"),
insert { "index" : 9 } first into collection delta-file("./dfile"),
insert { "index" : 10 } first into collection delta-file("./dfile"));
count(delta-file("./dfile"))
