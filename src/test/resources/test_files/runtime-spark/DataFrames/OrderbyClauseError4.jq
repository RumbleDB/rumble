(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
for $i in parallelize((1,2,3))
let $j := (1, 2)
order by $j
return $i

(: sequence of multiple items :)
