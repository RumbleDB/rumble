(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: The declared type of a for variable is checked statically. Never called, so the error is static. :)
declare function local:f() { for $x as string in (1, 2) return $x };
1
