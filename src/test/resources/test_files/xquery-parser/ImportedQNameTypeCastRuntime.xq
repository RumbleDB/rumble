(:JIQS: ShouldRun; Output="urn:example:compiled-schema" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
namespace-uri-from-QName("ex:value" cast as ex:QNameAlias)
