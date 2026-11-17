(:JIQS: ShouldRun; Output="(1, 3, after)" :)
declare function local:f($x) {
    if ($x eq 2) then exit returning (); else ();
    $x
};

(local:f(1), local:f(2), local:f(3), "after")

(: An exit statement can return the empty sequence. :)
