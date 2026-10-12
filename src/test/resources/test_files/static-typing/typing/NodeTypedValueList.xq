(:JIQS: ShouldRun; Output="(false, true, true)" :)
import schema namespace t = "urn:arguments" at "../../xquery-parser/schema-function-conversion/Arguments.xsd";

let $list := validate strict { <t:numbers>1 2</t:numbers> }
return (
    $list castable as xs:integer,
    try { $list cast as xs:integer } catch err:XPTY0004 { true() },
    ($list = 2) is statically xs:boolean
)
