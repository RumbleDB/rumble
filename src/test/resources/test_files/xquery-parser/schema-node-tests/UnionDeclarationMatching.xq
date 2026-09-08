(:JIQS: ShouldRun; Output="true" :)
import schema namespace t = "urn:declarations" at "Declarations.xsd";
(: Local integer annotations match a global pure union, but not a restricted union. :)
let $root := validate strict {
    <t:unionRoot t:unionCode="12" t:restrictedCode="12"><t:unionMember>12</t:unionMember><t:restricted>12</t:restricted></t:unionRoot>
}
return every $check in (
    $root/t:unionMember instance of schema-element(t:unionHead),
    $root/@t:unionCode instance of schema-attribute(t:unionCode),
    count($root/schema-element(t:unionHead)) eq 1,
    count($root/schema-attribute(t:unionCode)) eq 1,
    not($root/t:restricted instance of schema-element(t:restricted)),
    not($root/@t:restrictedCode instance of schema-attribute(t:restrictedCode))
) satisfies $check
