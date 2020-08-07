(:JIQS: ShouldNotCompile; ErrorCode="XPST0008";  ErrorMetadata="LINE:3:COLUMN:23:" :)
let $x := 2
let $y := function() { $z }
let $z := 4
return $y()
