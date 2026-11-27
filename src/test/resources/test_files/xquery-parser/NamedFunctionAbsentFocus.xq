(:JIQS: ShouldRun; Output="true":)
(: A named call inside a simple map converts arguments in the caller's focus.
   The function body starts without focus, even when inlining is enabled. :)
declare function local:read-focus($value as xs:integer) {
    ($value, .)
};

10 ! (try { local:read-focus(.) } catch err:XPDY0002 { . eq 10 })
