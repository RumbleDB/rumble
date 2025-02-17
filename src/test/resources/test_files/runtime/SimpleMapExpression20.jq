(:JIQS: ShouldRun; Output="100000" :)
count(parallelize(1 to 100000) ! ($$ * 2))

(: empty sequence :)
