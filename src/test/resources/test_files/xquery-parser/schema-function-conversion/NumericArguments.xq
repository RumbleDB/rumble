(:JIQS: ShouldRun; Output="true" :)
import schema namespace t = "urn:arguments" at "Arguments.xsd";
let $number := validate strict { <t:integer>-12</t:integer> }
let $abs := abs#1
return every $check in (
    abs($number) eq 12,
    abs($number) instance of xs:integer,
    $abs($number) eq 12,
    abs(<value>-3.5</value>) instance of xs:double,
    abs(<value>-3.5</value>) eq 3.5,
    empty(abs(())),
    empty(abs(validate strict { <t:numbers/> }))
) satisfies $check
