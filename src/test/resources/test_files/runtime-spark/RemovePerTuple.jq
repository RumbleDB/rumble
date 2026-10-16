(:JIQS: ShouldRun; Output="(20, 30, 10, 30)" :)
for $x in (1, 2)
return remove(parallelize((10, 20, 30)), $x)

(: each tuple builds its own RDD, so each must keep its own position :)
