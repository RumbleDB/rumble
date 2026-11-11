(:JIQS: ShouldCrash; ErrorCode="JNUP0008";  UpdateDim=[9,2]; ErrorMetadata="LINE:2:COLUMN:0:" :)
insert { "BEFORE" : "works" } before delta-file("./dfile-empty")[1] into collection,
insert { "AFTER" : "well" } after delta-file("./dfile-empty")[1] into collection