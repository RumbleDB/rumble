(:JIQS: ShouldRun; Output="(exited, 3, after)" :)
declare function local:f($acc, $x) {
    if ($x eq 2) then exit returning "exited"; else ();
    ($acc, $x)
};

(fold-left((1, 2, 3), (), local:f#2), "after")

(: An exit statement in a function called by fold-left ends only that call. :)
