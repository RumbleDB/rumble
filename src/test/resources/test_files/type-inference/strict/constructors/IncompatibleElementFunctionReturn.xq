(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare function local:f($y as xs:string) as element(e) {
    element other {$y}
};
local:f("name")
