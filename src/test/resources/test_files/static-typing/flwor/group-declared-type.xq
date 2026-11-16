(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: XQuery binds a grouping key with a let, and its declared type is still checked statically. Never called, so the error is static. :)
declare function local:group() { for $x in (1, 2) group by $k as xs:string := $x return $k };
1
