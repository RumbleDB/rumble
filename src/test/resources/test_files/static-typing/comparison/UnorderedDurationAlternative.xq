(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: General xs:duration is not ordered, even when both operands are the same variable. :)
for $x in (xs:duration("P1D"), 1)
return $x lt $x
