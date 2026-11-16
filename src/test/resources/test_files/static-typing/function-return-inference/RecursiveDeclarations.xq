(:JIQS: ShouldRun; Output="(1, 2)" :)
(: Neither self-recursion nor mutual recursion is narrowed by declaration order. :)
declare function local:self($n as xs:integer) {
    if ($n eq 0) then 1 else count(local:self($n - 1))
};
declare function local:first($n as xs:integer) {
    if ($n eq 0) then 2 else count((local:second#1)($n - 1))
};
declare function local:second($n as xs:integer) { (local:first#1)($n) };
local:self(1) is statically item()*,
(local:first#1)(0) is statically item()*
