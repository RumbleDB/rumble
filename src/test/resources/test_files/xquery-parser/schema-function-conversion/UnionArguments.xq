(:JIQS: ShouldRun; Output="true" :)
import schema namespace t = "urn:arguments" at "Arguments.xsd";
declare function local:one($value as t:NumberOrBoolean) { $value };
declare function local:many($values as t:NumberOrBoolean*) { $values };

let $number := validate strict { <t:integer>12</t:integer> }
let $list := validate strict { <t:numbers>1 2 3</t:numbers> }
let $one := local:one#1
let $many := local:many#1
return every $check in (
    local:one($number) eq 12,
    $one($number) eq 12,
    local:one(<value>true</value>) instance of xs:boolean,
    $one(<value>true</value>) instance of xs:boolean,
    deep-equal(local:many($list), (1, 2, 3)),
    deep-equal($many($list), (1, 2, 3)),
    deep-equal($many((<value>true</value>, <value>12</value>)), (true(), 12))
) satisfies $check
