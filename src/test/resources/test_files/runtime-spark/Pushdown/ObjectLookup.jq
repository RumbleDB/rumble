(:JIQS: ShouldRun; Output="(bar, [ 1, 2, 3 ])" :)
parallelize(({"foo":"bar", "bar" : "foo"},{},{"bar":"foo"},{"foo":[1, 2, 3]})).foo
