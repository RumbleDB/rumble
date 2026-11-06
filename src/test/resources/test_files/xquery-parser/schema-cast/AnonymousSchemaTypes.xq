(:JIQS: ShouldRun; Output="(true, true, true, true, true, true)" :)
import schema namespace t = "urn:anonymous-types" at "AnonymousSchemaTypes.xsd";
let $count := validate strict { <t:count>4</t:count> }
let $counts := validate strict { <t:counts>1 2 3</t:counts> }
let $choice := validate strict { <t:choice>ABC</t:choice> }
let $amount := validate strict { <t:amount unit="kg">12.5</t:amount> }
let $restricted := validate strict { <t:restricted>ABC</t:restricted> }
return (
    data($count) instance of xs:integer and data($count) eq 4,
    $count instance of element(*, xs:integer),
    data($counts) instance of xs:integer+ and deep-equal(data($counts), (1, 2, 3)),
    data($choice) instance of xs:string and data($choice) eq "ABC",
    data($amount) eq 12.5 and data($amount/@unit) eq "kg",
    data($restricted) instance of xs:string and data($restricted) eq "ABC"
)
