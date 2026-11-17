(:JIQS: ShouldRun; Output="200000" :)
count(parallelize(parallelize(1 to 200000)))

(: a distributed input is not collected, so it is not limited by the materialization cap (100000 in these tests) :)
