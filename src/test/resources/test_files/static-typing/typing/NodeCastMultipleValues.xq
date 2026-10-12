(:JIQS: ShouldRun; Output="true" :)
(: The cast's cardinality check is performed during evaluation. :)
try {
    (<a>1</a>, <b>2</b>) cast as xs:integer
} catch err:XPTY0004 {
    true()
}
