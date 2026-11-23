(:JIQS: ShouldRun; Output="(1, true)" :)
(: A lookup with several keys returns a value for each of them, so = stays a general comparison. :)
declare function local:f($m as map(xs:integer, xs:integer)*) { count($m[.?(1 to 2) = 3]) };
declare function local:g($a as array(xs:integer)) { $a?(1 to 2) = 20 };
(local:f((map{1: 1, 2: 2}, map{1: 2, 2: 3})), local:g([10, 20]))
