(:JIQS: ShouldRun; Output="(1, exited, after)" :)
let $f := function($x) {
    if ($x eq 2) then exit returning "exited"; else ();
    $x
}
return ($f(1), $f(2), "after")

(: An exit statement in an anonymous function ends only the call. :)
