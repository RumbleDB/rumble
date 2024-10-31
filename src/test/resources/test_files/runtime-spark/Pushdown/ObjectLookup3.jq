(:JIQS: ShouldRun; Output="{ "foo" : 1 }" :)
parallelize(({"foo":"bar"},{"foo":"foo"},{"foo":[1, 2, 3]}, [1, 2, 3], 1, 2, {"bar": { "foo" : 1 } })).bar
