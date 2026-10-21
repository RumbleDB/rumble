(:JIQS: ShouldRun; Output="(true, true)" :)
import schema namespace s = "urn:single-root" at "SingleRoot.xsd";

(: fn:doc returns a document node, so validating it gives the root the type of its global declaration. :)
(
    exists(doc("SingleRoot.xml") is statically document-node()?),
    exists((validate { doc("SingleRoot.xml") })/s:root is statically element(s:root, xs:integer))
)
