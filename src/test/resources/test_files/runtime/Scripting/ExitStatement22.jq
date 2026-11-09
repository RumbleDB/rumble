(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare function local:f($x) as integer {
    if ($x eq 2) then exit returning "not an integer"; else ();
    $x
};

(local:f(1), local:f(2), "after")

(: The result of an exit statement must match the declared return type. :)
