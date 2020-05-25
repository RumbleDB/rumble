(:JIQS: ShouldRun; Output="true" :)
remove(parallelize(1 to 10000), 4)[4] eq 5

(: parallelized empty sequence  :)
