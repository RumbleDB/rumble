(:JIQS: ShouldRun; Output="(item3, item4, item5)" :)
let $y := 4
let $x := 3
return subsequence(parallelize(("item1", "item2", "item3", "item4", "item5")), $x, $y)

(: using variables :)
