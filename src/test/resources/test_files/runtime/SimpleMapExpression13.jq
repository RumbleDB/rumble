(:JIQS: ShouldRun; Output="(#2, #4, #6, #8, #10)" :)
parallelize(1 to 5) ! ($$ * 2) ! (concat("#", string($$)))

(: multiple simple map statements :)
