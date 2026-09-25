(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true)" :)
import schema namespace t = "urn:identity" at "Identity.xsd";
let $doc := validate strict { document {
    <t:root><t:entry key="alpha" refs="beta clé"/><t:entry key="beta"/>
        <t:key>clé</t:key><t:ref>alpha</t:ref><t:refs>alpha beta</t:refs></t:root>
} }
return (
    deep-equal(idref("alpha", $doc), ($doc/t:root/t:ref, $doc/t:root/t:refs)),
    idref("clé", $doc) is $doc/t:root/t:entry[1]/@refs,
    deep-equal(idref(("beta", "alpha", "alpha"), $doc),
        ($doc/t:root/t:entry[1]/@refs, $doc/t:root/t:ref, $doc/t:root/t:refs)),
    count(idref("alpha", $doc/t:root/t:entry[1]/@key)) eq 2,
    count($doc/idref("alpha")) eq 2,
    (: Unlike id(), idref() does not split each argument string into several IDs. :)
    empty(idref("alpha beta", $doc)),
    empty(idref(("missing", "p:name", "1invalid"), $doc)),
    empty(idref((), $doc))
)
