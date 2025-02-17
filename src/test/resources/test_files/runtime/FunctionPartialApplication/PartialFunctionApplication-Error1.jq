(:JIQS: ShouldCrash; ErrorCode="XPTY0004";  ErrorMetadata="LINE:4:COLUMN:0:" :)
declare function fn ($i, $j) {$i + $j};
fn(3,5),
fn(3, ? ) (7,10)

(: Partial function application with inline function definition - incorrect number of parameters:)

