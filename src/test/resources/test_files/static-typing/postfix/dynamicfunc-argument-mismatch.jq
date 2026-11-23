jsoniq version "3.1";
(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare function a($b as xs:string) { $b };
declare variable $a := a#1;
(: Never called, so only static typing reports the argument. :)
declare function b() { $a(1) };
1
