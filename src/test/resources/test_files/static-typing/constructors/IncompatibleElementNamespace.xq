(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare function local:f($y as xs:string) as element(e) {
    <e xmlns="urn:other">{$y}</e>
};
local:f("name")
