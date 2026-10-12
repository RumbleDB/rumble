(:JIQS: ShouldCrash; ErrorCode="FORG0006" :)
for $var in ("a", "b", "c")
where fn:min(($var, 1))
return $var
