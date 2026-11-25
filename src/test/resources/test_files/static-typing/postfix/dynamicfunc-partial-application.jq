jsoniq version "3.1";
(:JIQS: ShouldRun; Output="a1b" :)
declare function a($b as xs:string, $c as xs:integer) { $b || $c };
declare variable $a := a#2;
($a(?, 1) is statically function(xs:string) as xs:string)("a") || "b"
