(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: Null cannot hide the string alternative, which cannot be compared with an integer. :)
for $x in (1, null, "a")
return $x eq 1
