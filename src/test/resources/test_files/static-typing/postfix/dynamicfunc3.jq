(:JIQS: ShouldCrash; ErrorCode="SENR0001" :)
declare function a($b as atomic) { "a" || $b };
declare variable $a := a#1;
$a is statically function(atomic) as string