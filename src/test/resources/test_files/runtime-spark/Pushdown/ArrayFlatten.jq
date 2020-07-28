(:JIQS: ShouldRun; Output="(3, 4, 5, 6, 1, 2, 3, foo, 4, { "foo" : "bar" }, 6, { }, -2, 2020-03-25, foo)" :)
flatten(parallelize(for $i in 1 to 1000 return [[3, 4], [5, 6], [[1, [2]]]]))[position() ge 499 and position() le 505],
flatten(parallelize(for $i in 1 to 1000 return [["foo", 4], [[{"foo" : "bar"}], 6], [[{}, [-2, [date("2020-03-25")]]]]]))[position() ge 498 and position() le 505]
