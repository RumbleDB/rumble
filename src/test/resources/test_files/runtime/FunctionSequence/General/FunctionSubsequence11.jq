(:JIQS: ShouldRun; Output="(item2, item3, item4, item5)" :)
subsequence(parallelize(("item1", "item2", "item3", "item4", "item5")), 2, 10)

(: parallelized length alone exceeding size :)
