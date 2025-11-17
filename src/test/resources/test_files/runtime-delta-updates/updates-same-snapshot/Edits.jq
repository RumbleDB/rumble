(:JIQS: ShouldCrash; ErrorCode="JNUP0012"; UpdateDim=[9,7]; ErrorMetadata="LINE:2:COLUMN:0:" :)
edit delta-file("./dfile")[1] into { "index" : 100 } in collection,
edit delta-file("./dfile")[1] into { "index" : 1 } in collection,
edit delta-file("./dfile")[1] into { "index" : 1000 } in collection,
edit delta-file("./dfile")[1] into { "index" : 0 } in collection