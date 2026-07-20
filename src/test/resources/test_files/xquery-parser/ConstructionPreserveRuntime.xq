(:JIQS: ShouldRun; Output="true" :)
declare construction preserve;
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $validated := validate type ex:Code { <value>ABC</value> }
let $copy := <wrapper>{$validated}</wrapper>
return
  $copy instance of element(wrapper, xs:anyType)
  and $copy/value instance of element(value, ex:Code)
  and data($copy/value) instance of ex:Code
  and not($copy/value is $validated)
