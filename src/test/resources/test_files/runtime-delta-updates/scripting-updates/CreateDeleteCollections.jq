(:JIQS: ShouldRun; UpdateDim=[10,0]; Output="" :)
create collection delta-file("./dfile1") with ();
create collection delta-file("./dfile2") with ();
create collection delta-file("./dfile3") with ();
(delete collection delta-file("./dfile1"),
delete collection delta-file("./dfile2"),
delete collection delta-file("./dfile3"));