(:JIQS: ShouldCrash; ErrorCode="SENR0001" :)
declare function a($b as anyAtomicType) { "a" || $b };
declare variable $a := a#1;
$a is statically function(anyAtomicType) as item*