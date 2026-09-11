(:JIQS: ShouldRun; Output="true" :)
import schema namespace t = "urn:declarations" at "Declarations.xsd";
declare function local:accept($node as schema-element(t:head)) as schema-element(t:head) { $node };
let $doc := validate strict { document {
    <t:root t:code="valid"><t:member>1</t:member><t:leaf>2</t:leaf>
        <t:nullable xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true"/>
        <t:blocked>3</t:blocked><t:blockedMember>4</t:blockedMember><t:anonymous>5</t:anonymous>
    </t:root>
} }
let $local := validate strict { <t:localRoot localAttribute="1"><t:member>1.5</t:member><t:head>2</t:head><t:local>3</t:local></t:localRoot> }
return every $check in (
    (: Sequence types and paths must use the same declaration, including indirect substitutions. :)
    $doc/t:root/t:member instance of schema-element(t:head),
    $doc/t:root/t:leaf instance of schema-element(t:head),
    count($doc/t:root/schema-element(t:head)) eq 3,
    exists($doc/t:root/schema-element(t:head)/self::t:leaf),
    local:accept($doc/t:root/t:leaf) is $doc/t:root/t:leaf,
    $doc/t:root/t:nullable instance of schema-element(t:nullable),
    $doc/t:root/t:anonymous instance of schema-element(t:anonymous),
    $doc instance of document-node(schema-element(t:root)),
    count($doc/self::document-node(schema-element(t:root))) eq 1,
    $doc/t:root/@t:code instance of schema-attribute(t:code),
    count($doc/t:root/schema-attribute(t:code)) eq 1,
    count($doc/t:root/attribute::schema-attribute(t:code)) eq 1,
    (: The member's own integer type is required; the head's decimal type is insufficient. :)
    not($local/t:member instance of schema-element(t:head)),
    not($local/t:head instance of schema-element(t:head)),
    not($doc/t:root/t:blockedMember instance of schema-element(t:blocked)),
    not(<t:member>1</t:member> instance of schema-element(t:member)),
    not(attribute t:code { "valid" } instance of schema-attribute(t:code)),
    not(document { <t:root/>, text { "extra" } } instance of document-node(schema-element(t:root)))
) satisfies $check
