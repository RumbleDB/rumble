(:JIQS: ShouldRun :)
declare function a($b as anyAtomicType) { "a" || $b };
declare variable $a := a#1;
$a(12) is statically item*