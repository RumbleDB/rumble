(:JIQS: ShouldCrash; ErrorCode="XPTY0004";  ErrorMetadata="LINE:4:COLUMN:27:" :)
declare function fn ($i as double?, $j as integer, $k as decimal) as double+ {$i + $j + $k};
for $i in parallelize((5, 10, "hello"))
return fn(3, ?, ?) (5, ?) ($i)
