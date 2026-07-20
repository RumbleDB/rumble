(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
data(validate type ex:CodeContent { <value> ABC </value> }) instance of xs:token
