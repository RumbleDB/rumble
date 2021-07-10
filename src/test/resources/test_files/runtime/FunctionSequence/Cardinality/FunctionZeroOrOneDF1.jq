(:JIQS: ShouldRun; Output="(1)" :)
zero-or-one(annotate(parallelize(1 to 1) ! { "foo" : $$ cast as int }, { "foo" : "double" }).foo)

