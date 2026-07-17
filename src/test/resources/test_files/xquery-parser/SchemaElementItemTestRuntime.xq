(:JIQS: ShouldRun; Output="true" :)
declare construction preserve;
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $element := validate strict { <ex:code-member>ABC</ex:code-member> }
let $document := document { $element }
return
  $element instance of schema-element(ex:code-head)
  and $element instance of schema-element(ex:code-member)
  and count($document/child::schema-element(ex:code-head)) = 1
  and not($element instance of schema-element(ex:nil-root))
