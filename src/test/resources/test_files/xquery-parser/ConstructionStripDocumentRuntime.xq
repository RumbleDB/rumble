(:JIQS: ShouldRun; Output="true" :)
declare construction strip;
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $validated := validate type ex:Code { <value>ABC</value> }
let $document := document { $validated }
return
  $document instance of document-node(element(value, xs:untyped))
  and data($document/value) instance of xs:untypedAtomic
  and not($document/value is $validated)
