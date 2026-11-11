(:JIQS: ShouldRun; Output="(1, 1, 1)" :)
(: An element has at most one attribute of each name. :)
declare function local:count($a as attribute()?) as xs:integer { count($a) };
declare function local:value($s as xs:string?) as xs:string? { $s };
let $e := <a x="1" y="2"/>
return (local:count($e/@x), local:count($e/attribute(y)), local:value($e/@x/string()))
