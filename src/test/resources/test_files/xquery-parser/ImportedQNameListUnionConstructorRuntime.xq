(:JIQS: ShouldRun; Output="urn:first,urn:second" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
declare namespace first = "urn:first";
declare namespace second = "urn:second";
string-join(
  ex:QNameListOrInteger("first:value second:value") ! namespace-uri-from-QName(.),
  ","
)
