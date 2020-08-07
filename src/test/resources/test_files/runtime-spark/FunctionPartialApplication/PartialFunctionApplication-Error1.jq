(:JIQS: ShouldCrash; ErrorCode="XPTY0004";  ErrorMetadata="LINE:4:COLUMN:7:" :)
declare function fn ($i, $j) {$i + $j};
for $i in parallelize((5, 10, 15))
return fn(3, ?) ($i, 5)


(: Partial function application with inline function definition - incorrect number of parameters:)

