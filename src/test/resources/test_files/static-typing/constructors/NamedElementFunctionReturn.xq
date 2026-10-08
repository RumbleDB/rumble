(:JIQS: ShouldRun; Output="(name, computed)" :)
declare function local:f($y as xs:string) as element(e) {
    <e y="{$y}"/>
};
declare function local:computed($y as xs:string) as element(e) {
    element e {$y}
};
(
    data(local:f("name")/@y),
    string(local:computed("computed"))
)
