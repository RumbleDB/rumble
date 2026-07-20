(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $code := data(validate type ex:CodeOrInteger { <value>ABC</value> })
let $integer := data(validate type ex:CodeOrInteger { <value>42</value> })
return
  $code instance of ex:CodeOrInteger
  and $integer instance of ex:CodeOrInteger
  and $code instance of ex:Code
  and $integer instance of xs:integer
