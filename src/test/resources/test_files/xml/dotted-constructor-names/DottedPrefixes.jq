(:JIQS: ShouldRun; Output="urn:q a.b, urn:1 c.d, urn:dot e.f" :)
let $element := <p.q:a.b xmlns:p.q="urn:q" xmlns:p.1="urn:1" xmlns:p.="urn:dot" p.1:c.d="" p.:e.f=""></p.q:a.b>
return string-join(
    for $node in ($element, $element/@*)
    return namespace-uri($node) || " " || local-name($node),
    ", "
)
