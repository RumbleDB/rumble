(:JIQS: ShouldRun; Output="true" :)
let $element := <p.q:a.b xmlns:p.q="urn:test" p.q:c.d="value"></p.q:a.b>
return (
    local-name($element) eq "a.b"
    and string(namespace-uri($element)) eq "urn:test"
    and local-name($element/@*) eq "c.d"
    and string(namespace-uri($element/@*)) eq "urn:test"
)
