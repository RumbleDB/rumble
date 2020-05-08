(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
for $i in parallelize((1,2,3))
group by $j := (1, 2)
return $i

(: sequence of multiple items :)
