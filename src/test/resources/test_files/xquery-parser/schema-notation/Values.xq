(:JIQS: ShouldRun; Output="true" :)
import schema namespace n = "urn:notation" at "Notation.xsd";
declare namespace alias = "urn:notation";
declare function local:accept($value as xs:NOTATION) { $value };
(: Validation and constructors retain the derived type. Equality ignores prefixes,
   while string conversion retains a lexical QName rather than an expanded-name string. :)
let $node := validate strict { <n:image xmlns:hidden="urn:notation">hidden:jpeg</n:image> }
let $value := data($node)
let $alias := n:Image("alias:jpeg")
return every $check in (
  $value instance of n:Image,
  $value instance of xs:NOTATION,
  not($value instance of xs:QName),
  $value eq $alias,
  $node = $alias,
  n:Image("n:png") ne $value,
  string($alias) eq "alias:jpeg",
  ($alias cast as xs:string) eq "alias:jpeg",
  n:Jpeg($value) eq $alias,
  (n:Jpeg($alias) cast as n:Image) eq $value,
  local:accept($node) eq $value,
  deep-equal($alias, $value),
  not(deep-equal($value, QName("urn:notation", "n:jpeg"))),
  count(distinct-values(($value, $alias))) eq 1
) satisfies $check
