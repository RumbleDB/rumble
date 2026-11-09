(:JIQS: ShouldRun; Output="(1, 20, true, after)" :)
declare function local:f($x) as double {
    if ($x eq 2) then exit returning 20; else ();
    $x
};

(local:f(1), local:f(2), local:f(2) instance of double, "after")

(: The result of an exit statement is converted to the declared return type, here with a single item. :)
