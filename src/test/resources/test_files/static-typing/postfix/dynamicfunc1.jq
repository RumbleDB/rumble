(:JIQS: ShouldRun :)
declare function a($b) { "a" || $b };
declare variable $a := a#1;
$a(12) is statically item*, $a(?) is statically function