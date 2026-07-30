(:JIQS: ShouldRun; Output="0":)
declare %assignable variable $x := 3;
declare function local:recursive_decrement_x($to_change) {
    if ($x gt 0) then {
        $x := $x - 1;
        local:recursive_decrement_x($to_change);
    } else {
        ();
    }
};
variable $res := 10;
local:recursive_decrement_x($res);
$x