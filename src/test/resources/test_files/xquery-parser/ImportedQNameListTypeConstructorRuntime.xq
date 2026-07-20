(:JIQS: ShouldRun; Output="first,second" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
string-join(
  ex:QNameList("ex:first ex:second") ! local-name-from-QName(.),
  ","
)
