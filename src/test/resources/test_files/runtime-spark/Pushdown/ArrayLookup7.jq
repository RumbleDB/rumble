(:JIQS: ShouldRun; Output="(1, 2)" :)
annotate(({ "foo" : [] } , { "foo" : [ 1 ] }, { "foo" : [ 2, 3 ] } ), { "foo" : [ "integer" ] }).foo[[1]]
