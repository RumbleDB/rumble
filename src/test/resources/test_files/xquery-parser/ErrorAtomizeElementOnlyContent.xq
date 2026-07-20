(:JIQS: ShouldCrash; ErrorCode="FOTY0012" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
data(validate type ex:Record { <value><ex:code>ABC</ex:code></value> })
