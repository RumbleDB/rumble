(:JIQS: ShouldRun; Output="(1, exited, 3, after)" :)
declare function local:f($x, $y) {
    if ($x eq 2) then exit returning "exited"; else ();
    $x
};

(for-each-pair((1, 2, 3), (1, 2, 3), local:f#2), "after")

(: An exit statement in a function called by for-each-pair ends only that call. :)
