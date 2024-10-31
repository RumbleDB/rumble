(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:2:COLUMN:10:" :)
for $f in parallelize(function($x) { $x }, function($x) { $x + 1 })
for $i in 1 to 2
return $f($i)

(: Non-integer partition given :)
