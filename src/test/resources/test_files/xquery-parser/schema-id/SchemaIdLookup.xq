(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true)" :)
import schema namespace t = "urn:identity" at "Identity.xsd";
let $doc := validate strict { document {
    <t:root><t:entry key="alpha" refs="beta clé"/><t:entry key="beta"/>
        <t:key>clé</t:key><t:ref>alpha</t:ref><t:refs>alpha beta</t:refs></t:root>
} }
return (
    id("alpha", $doc) is $doc/t:root/t:entry[1],
    id("clé", $doc) is $doc/t:root/t:key,
    (: Attribute context still searches the entire containing document. :)
    id("beta", $doc/t:root/t:entry[1]/@key) is $doc/t:root/t:entry[2],
    deep-equal(id(("clé alpha", "beta alpha"), $doc), ($doc/t:root/t:entry, $doc/t:root/t:key)),
    count($doc/id("alpha")) eq 1,
    empty(id(("missing", "1invalid", "p:name"), $doc)),
    empty(id((), $doc)),
    id("  alpha&#x9;beta&#xA;", $doc)[1] is $doc/t:root/t:entry[1]
)
