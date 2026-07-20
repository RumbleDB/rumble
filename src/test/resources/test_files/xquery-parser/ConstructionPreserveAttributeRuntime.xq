(:JIQS: ShouldRun; Output="true" :)
declare construction preserve;
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $validated := validate strict { <ex:root status="ready">ABC</ex:root> }
let $copy := <wrapper>{$validated}</wrapper>
return $copy/ex:root/@status instance of attribute(status, xs:string)
