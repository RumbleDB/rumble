(:JIQS: ShouldRun; UpdateDim=[9,3]; Output="{ "index" : 1 }" :)
(create collection delta-file("./dfile") with { "index" : 1 });
delta-file("./dfile")