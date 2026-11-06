(:JIQS: ShouldRun; Output="(1, 3, 2, 4)" :)
for $x in (1, 2)
return index-of(parallelize((1, 2, 1, 2)), $x)

(: each tuple builds its own RDD, so each must keep its own search value :)
