(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: An inlined call checks an argument against its parameter type, also when it is a variable named like the parameter. :)
declare function local:f($x as xs:string) { $x };
let $x := 1
return local:f($x)
