(:JIQS: ShouldRun; Output="(3, 4, 5, 6, 1, 2, 3)" :)
flatten(parallelize(for $i in 1 to 1000 return [[3, 4], [5, 6], [[1, [2]]]]))[499],
flatten(parallelize(for $i in 1 to 1000 return [[3, 4], [5, 6], [[1, [2]]]]))[500],
flatten(parallelize(for $i in 1 to 1000 return [[3, 4], [5, 6], [[1, [2]]]]))[501],
flatten(parallelize(for $i in 1 to 1000 return [[3, 4], [5, 6], [[1, [2]]]]))[502],
flatten(parallelize(for $i in 1 to 1000 return [[3, 4], [5, 6], [[1, [2]]]]))[503],
flatten(parallelize(for $i in 1 to 1000 return [[3, 4], [5, 6], [[1, [2]]]]))[504],
flatten(parallelize(for $i in 1 to 1000 return [[3, 4], [5, 6], [[1, [2]]]]))[505]
 