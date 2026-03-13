(:JIQS: ShouldRun; UpdateDim=[10,1]; Output="{ "x" : 1 }" :)
create collection delta-file("./dfile1") with { "x": 1 };
delta-file("./dfile1")