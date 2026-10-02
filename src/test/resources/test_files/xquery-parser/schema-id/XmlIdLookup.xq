(:JIQS: ShouldRun; Output="(true, true, true, true)" :)
let $doc := document { <root><a xml:id="clé"/><b xml:id="second"/><c xml:id="clé"/></root> }
return (
    id("clé", $doc) is $doc/root/a,
    deep-equal(id(("second clé", "clé"), $doc), ($doc/root/a, $doc/root/b)),
    empty(id("missing", $doc)),
    empty(idref("clé", $doc))
)
