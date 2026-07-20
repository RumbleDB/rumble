(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
every $name in ex:QNameList("ex:first ex:second")
satisfies namespace-uri-from-QName($name) eq "urn:example:compiled-schema"
