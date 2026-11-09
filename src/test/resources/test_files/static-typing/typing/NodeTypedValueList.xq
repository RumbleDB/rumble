(:JIQS: ShouldRun; Output="(false, true)" :)
import schema namespace t = "urn:arguments" at "../../xquery-parser/schema-function-conversion/Arguments.xsd";

let $list := validate strict { <t:numbers>1 2</t:numbers> }
return (
    $list castable as xs:integer,
    ($list = 2) is statically xs:boolean
)
