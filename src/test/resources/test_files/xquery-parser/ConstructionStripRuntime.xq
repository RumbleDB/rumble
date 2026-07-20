(:JIQS: ShouldRun; Output="true" :)
declare construction strip;
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $validated := validate strict { <ex:root status="ready">ABC</ex:root> }
let $copy := element wrapper { $validated }
return
  $copy instance of element(wrapper, xs:untyped)
  and $copy/ex:root instance of element(ex:root, xs:untyped)
  and $copy/ex:root/@status instance of attribute(status, xs:untypedAtomic)
  and data($copy/ex:root) instance of xs:untypedAtomic
  and not($copy/ex:root is $validated)
