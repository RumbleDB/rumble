(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: The declared type of a variable declaration statement is checked statically. Never called, so the error is static. :)
declare function local:f() { variable $x as string := 1; $x };
1
