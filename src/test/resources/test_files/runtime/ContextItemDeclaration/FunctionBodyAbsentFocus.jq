(:JIQS: ShouldRun; Output="true":)
declare context item := 10;
declare function local:read-focus($value as integer) {
    ($value, $$)
};

let $named := try { local:read-focus($$) } catch err:XPDY0002 { $$ eq 10 }
let $inline := try { (function() { $$ })() } catch err:XPDY0002 { $$ eq 10 }
return $named and $inline
