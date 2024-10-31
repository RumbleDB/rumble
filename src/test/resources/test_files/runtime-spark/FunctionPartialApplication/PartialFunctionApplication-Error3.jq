(:JIQS: ShouldCrash; ErrorCode="XPTY0004";  ErrorMetadata="LINE:4:COLUMN:7:" :)
declare function fn ($i as double?, $j as integer, $k as decimal) as string+ {$i + $j + $k};
for $i in parallelize((5, 10, 3))
return fn(3, ?, ?) (5, ?) ($i)

(: Invalid return type for function. double cannot be promoted to type string+. :)
