(:JIQS: ShouldRun; UpdateDim=[10,2]; Output="{ "a" : 1 }":)
create collection delta-file("./dfile2") with ();
insert { "a": 1 } last into collection delta-file("./dfile2");
delta-file("./dfile2")