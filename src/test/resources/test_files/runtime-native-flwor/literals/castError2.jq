(:JIQS: ShouldCrash; ErrorCode="FORG0001" :)
for $i in parallelize((1 to 10), 10)
let $a := xs:boolean("falsea")
return $a
