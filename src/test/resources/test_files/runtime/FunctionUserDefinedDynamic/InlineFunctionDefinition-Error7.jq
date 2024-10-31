(:JIQS: ShouldCrash; ErrorCode="XPTY0004";  ErrorMetadata="LINE:2:COLUMN:0:" :)
function($i as string?, $j as double) as object* {$i || ($j cast as string)} ("hello", 2e3)

(: Invalid return type. string cannot be promoted to type object*. :)
