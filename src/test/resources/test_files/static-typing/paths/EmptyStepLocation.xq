(:JIQS: ShouldCrash; ErrorCode="XPST0005"; ErrorMetadata="LINE:4:COLUMN:13:" :)
(: A text node has no children, so the error points at the step. :)
let $text := text { "x" }
return $text/child
