(:JIQS: ShouldRun; Output="true" :)
let $element := <p.q:outer.name xmlns:p.q="urn:outer"><p.q:inner.name xmlns:p.q="urn:inner"/><p.q:sibling.name/></p.q:outer.name>
return (
    string(namespace-uri($element)) eq "urn:outer"
    and count($element/*) eq 2
    and local-name($element/*[1]) eq "inner.name"
    and string(namespace-uri($element/*[1])) eq "urn:inner"
    and local-name($element/*[2]) eq "sibling.name"
    and string(namespace-uri($element/*[2])) eq "urn:outer"
)
