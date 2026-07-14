(:JIQS: ShouldParse :)
declare function local:f($x as xs:long, $y as xs:NCName) as element(e) {
    <e x="{$x}" y="{$y}"/>
};
local:f(1, "name")
