(:JIQS: ShouldRun; Output="(true, true, true)" :)
import schema namespace s = "urn:single-root" at "SingleRoot.xsd";

(: Strict validation requires a global declaration with the root's name, and the only one in scope is s:root. :)
let $root := document { <s:root>1</s:root> }
return (
    exists(validate { $root } is statically document-node(schema-element(s:root))),
    exists(validate lax { $root } is statically document-node()),
    exists(validate type xs:integer { document { <total>1</total> } } is statically document-node(element(*, xs:integer)))
)
