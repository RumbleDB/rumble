jsoniq version "3.1";
(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare function a($b as xs:string, $c as xs:string) { $b || $c };
declare variable $a := a#2;
(: Never called, so only static typing reports the supplied argument of the partial application. :)
declare function b() { $a(1, ?) };
1
