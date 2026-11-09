(:JIQS: ShouldRun; Output="(0, 10, 20, 30, 10, 0, 20, 30)" :)
for $x in (1, 2)
return insert-before(parallelize((10, 20, 30)), $x, parallelize(0))

(: each tuple builds its own RDD, so each must keep its own position :)
