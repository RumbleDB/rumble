(:JIQS: ShouldRun; Output="urn:test" :)
declare namespace p = "urn:test";

let $element := <e xmlns:p="urn:test" p:a="v"/>
return string(namespace-uri($element/@p:a))
