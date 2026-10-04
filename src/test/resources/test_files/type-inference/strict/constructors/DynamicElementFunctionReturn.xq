(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: A string parameter cannot guarantee the expanded name of the constructed element. :)
declare function local:f($name as xs:string) as element(e) {
    element {$name} {}
};
local:f("e")
