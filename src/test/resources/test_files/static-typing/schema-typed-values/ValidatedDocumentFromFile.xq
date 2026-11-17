(:JIQS: ShouldRun; Output="(true, true)" :)
import schema namespace s = "urn:single-root" at "SingleRoot.xsd";

(: A literal fn:doc URI exposes the root name; validation adds its schema type annotation. :)
(
    exists(doc("SingleRoot.xml") is statically document-node(element(s:root))),
    exists((validate { doc("SingleRoot.xml") })/s:root is statically element(s:root, xs:integer))
)
