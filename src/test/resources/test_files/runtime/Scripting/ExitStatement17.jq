(:JIQS: ShouldRun; Output="(1, 3, 2, after)" :)
declare function local:key($x) {
    if ($x eq 2) then exit returning 10; else ();
    $x
};

(sort((3, 2, 1), (), local:key#1), "after")

(: The result of an exit statement in a sort key function is the key. :)
