(:JIQS: ShouldRun; Output="(true, true, true)" :)
declare default element namespace "urn:default";
declare namespace p = "urn:test";
declare namespace q = "urn:test";
declare function local:default($y as xs:string) as element(e) {
    <e>{$y}</e>
};
declare function local:alias($y as xs:string) as element(p:e) {
    <alias:e xmlns:alias="urn:test">{$y}</alias:e>
};
declare function local:computed($y as xs:string) as element(p:e) {
    element q:e {$y}
};
(
    local:default("name") instance of element(e),
    local:alias("name") instance of element(p:e),
    local:computed("name") instance of element(p:e)
)
