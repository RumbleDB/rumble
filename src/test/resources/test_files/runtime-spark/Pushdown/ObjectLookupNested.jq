(:JIQS: ShouldRun; Output="([ 1, 2, 3 ], 3)" :)
parallelize(({"foo":"bar", "bar" : "foo"},{ "foo" : { "bar" : [ 1, 2, 3 ] } },{"bar":"foo"},{"foo": { "bar" : 3 } })).foo.bar
