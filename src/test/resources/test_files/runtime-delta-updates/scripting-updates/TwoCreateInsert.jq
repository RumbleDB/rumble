(:JIQS: ShouldRun; UpdateDim=[10,9]; Output="({ "c" : "A", "v" : 1 }, { "c" : "B", "v" : 10 }, { "c" : "B", "v" : 20 })" :)
create collection delta-file("./dfile9_1") with ();
create collection delta-file("./dfile9_2") with { "c": "B", "v": 10 };

insert { "c": "A", "v": 1 } last into collection delta-file("./dfile9_1");
insert { "c": "B", "v": 20 } last into collection delta-file("./dfile9_2");

delta-file("./dfile9_1"), delta-file("./dfile9_2")