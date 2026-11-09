(:JIQS: ShouldRun; Output="(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)" :)
one-or-more(annotate(parallelize(1 to 10) ! { "foo" : $$ cast as int }, { "foo" : "double" }).foo)

