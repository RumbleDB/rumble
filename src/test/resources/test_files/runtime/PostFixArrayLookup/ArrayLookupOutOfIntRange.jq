(:JIQS: ShouldRun; Output="(0, 0)" :)
(: 4294967297 must not wrap around to position 1, locally or on an RDD. :)
(count([1, 2, 3][[4294967297]]), count(parallelize([1, 2, 3])[[4294967297]]))
