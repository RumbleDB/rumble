(:JIQS: ShouldRun; Output="true" :)
let $element := <a.b xmlns="urn:default" attr.name="value"/>
return (
    local-name($element) eq "a.b"
    and string(namespace-uri($element)) eq "urn:default"
    and local-name($element/@*) eq "attr.name"
    and string(namespace-uri($element/@*)) eq ""
)
