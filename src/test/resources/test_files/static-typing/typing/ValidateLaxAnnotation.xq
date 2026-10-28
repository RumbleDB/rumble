(:JIQS: ShouldRun; Output="true" :)
(: Validation annotates its copy anew, so the copy of an element(a, xs:untyped) is only known to be an element(a). :)
declare function local:validate($e as element(a, xs:untyped)) as element(a) {
    validate lax { $e } is statically element(a)
};
local:validate(<a/>) instance of element(a)
