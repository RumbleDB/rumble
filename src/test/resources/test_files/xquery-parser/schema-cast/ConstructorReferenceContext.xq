(:JIQS: ShouldRun; Output="(true, true, true)" :)
declare namespace p = "urn:constructor";

(: Calling a constructor in a different namespace scope must not rebind its static context. :)
let $named := xs:QName#1
let $lookedUp := function-lookup(QName("http://www.w3.org/2001/XMLSchema", "QName"), 1)
let $partial := $named(?)
return (
    string(<value xmlns:p="urn:caller">{namespace-uri-from-QName($named("p:name"))}</value>) eq "urn:constructor",
    string(<value xmlns:p="urn:caller">{namespace-uri-from-QName($lookedUp("p:name"))}</value>) eq "urn:constructor",
    string(<value xmlns:p="urn:caller">{namespace-uri-from-QName($partial("p:name"))}</value>) eq "urn:constructor"
)
