(:JIQS: ShouldRun; Output="true" :)
let $element := <p.:a.b xmlns:p.="urn:test" p.:c.d="value"></p.:a.b>
return (
    local-name($element) eq "a.b"
    and string(namespace-uri($element)) eq "urn:test"
    and local-name($element/@*) eq "c.d"
    and string(namespace-uri($element/@*)) eq "urn:test"
)
