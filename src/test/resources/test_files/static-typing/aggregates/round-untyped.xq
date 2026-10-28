(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: fn:round converts an untyped value to xs:double, so its result is not xs:untypedAtomic. local:g is not called,
   so the error comes from the static check. :)
declare function local:f($x as xs:untypedAtomic) { $x };
declare function local:g() { local:f(round(xs:untypedAtomic("1.5"))) };
1
