(:JIQS: ShouldRun; Output="true" :)
let $element := <p.1:a.b xmlns:p.1="urn:test" p.1:c.d="value"></p.1:a.b>
return (
    local-name($element) eq "a.b"
    and string(namespace-uri($element)) eq "urn:test"
    and local-name($element/@*) eq "c.d"
    and string(namespace-uri($element/@*)) eq "urn:test"
)
