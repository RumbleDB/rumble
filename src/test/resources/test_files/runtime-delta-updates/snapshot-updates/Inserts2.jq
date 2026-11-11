(:JIQS: ShouldRun; UpdateDim=[9,6]; Output="({ "FIRSTTTT" : "at the very beginning" }, { "before-1" : "right BEFORE row 1" }, { "should-be-first" : 9789 }, { "after-1" : "right after row 1" }, { "before-2" : "should be before row 2" }, { "index" : 1 }, { "after-2" : "should be AFTER row 2" }, { "before-3" : "should be before row 3" }, { "latest" : "laster" }, { "after-3" : "test after row 3" }, { "LASTTTT" : "does it work??" })" :)
(insert {"before-2": "should be before row 2"} before delta-file("./dfile")[2] into collection,
insert {"after-1": "right after row 1"} after delta-file("./dfile")[1] into collection,
insert {"after-3": "test after row 3"} after delta-file("./dfile")[3] into collection,
insert {"before-1": "right BEFORE row 1"} before delta-file("./dfile")[1] into collection,
insert {"after-2": "should be AFTER row 2"} after delta-file("./dfile")[2] into collection,
insert {"before-3": "should be before row 3"} before delta-file("./dfile")[3] into collection,
insert {"LASTTTT": "does it work??"} last into collection delta-file("./dfile"),
insert {"FIRSTTTT": "at the very beginning"} first into collection delta-file("./dfile"));
delta-file("./dfile")
