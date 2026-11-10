(:JIQS: ShouldRun; Output="(42, 42)" :)
(: Function references keep this test independent of function inlining. :)
declare function local:outer() { (local:answer#0)() };
declare function local:answer() { 42 };
declare variable $answer := (local:outer#0)();
$answer is statically xs:integer,
(local:outer#0)() is statically xs:integer
