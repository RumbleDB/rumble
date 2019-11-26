(:JIQS: ShouldCrash; ErrorCode="XPDY0130"; :)
for $j in parallelize((duration("P12Y"), 123, "Hello"))
order by $j
return $j
