(:JIQS: ShouldRun; Output="<e x="1" y="name">\n</e>\n" :)
declare function local:f($x as xs:long, $y as xs:string) as element(e) {
    <e x="{$x}" y="{$y}"/>
};
local:f(1, "name")
