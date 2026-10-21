(:JIQS: ShouldRun; Output="(1, 3)" :)
(: The distributed input exceeds the Spark suite's materialization cap of 100000. :)
head(parallelize(1 to 100001)),
count(subsequence(parallelize(1 to 100001), 1, 3))
