(:JIQS: ShouldRun; Output="ABC" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
declare function local:identity($value as ex:Code) as ex:Code { $value };
local:identity(data(validate type ex:Code { <value>ABC</value> }))
