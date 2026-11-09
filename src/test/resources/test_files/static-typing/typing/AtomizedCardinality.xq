(:JIQS: ShouldRun; Output="(true, true, true, 7, 7, true, true)" :)
import schema namespace t = "urn:typed-node-test" at "../../xquery-parser/schema-cast/TypedNodeTests.xsd";

let $empty := validate strict {
    <t:root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" count="0" xsi:nil="true"/>
}
return (
    empty(($empty cast as xs:integer?) is statically xs:integer?),
    $empty castable as xs:integer?,
    not($empty castable as xs:integer),
    (($empty, 7) cast as xs:integer) is statically xs:integer,
    ((7, $empty) cast as xs:integer) is statically xs:integer,
    ($empty, 7) castable as xs:integer,
    try { $empty cast as xs:integer } catch err:XPTY0004 { true() }
)
