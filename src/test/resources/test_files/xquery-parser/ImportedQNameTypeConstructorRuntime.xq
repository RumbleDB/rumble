(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
ex:QNameAlias("ex:value") eq QName("urn:example:compiled-schema", "ex:value")
