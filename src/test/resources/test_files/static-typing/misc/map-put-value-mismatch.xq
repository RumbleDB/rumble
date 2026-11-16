(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: local:f is not called, so the error comes from the static check. :)
declare function local:f() as map(xs:string, xs:integer) { map:put(map { "a": 1 }, "b", "x") };
1
