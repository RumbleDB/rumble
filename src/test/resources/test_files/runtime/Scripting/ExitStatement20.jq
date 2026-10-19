(:JIQS: ShouldRun; Output="(1, outer, inner, outer, after)" :)
declare function local:inner($x) {
    if ($x eq 2) then exit returning "inner"; else ();
    $x
};

declare function local:outer($x) {
    (local:inner($x), "outer")
};

(local:outer(1), local:outer(2), "after")

(: An exit statement ends only the function that contains it, not its caller. :)
