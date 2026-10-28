(:JIQS: ShouldRun; Output="(2, 3)" :)
(: A member that is one item makes a JSON array, and one that is a sequence an array of sequences. :)
declare function local:size($a as array(xs:integer)) as xs:integer { array:size($a) };
declare function local:count($a as array(xs:integer*)) as xs:integer { count($a?*) };
(local:size([1, 2]), local:count([1, (2, 3), ()]))
