(:JIQS: ShouldRun; Output="3" :)
(: Consume a small slice of an input exceeding the Spark suite's materialization cap. :)
count((subsequence#3)(parallelize(1 to 100001), 1, 3))
