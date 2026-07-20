(:JIQS: ShouldRun; Output="ABC,DEF" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
declare function local:codes($value as xs:string) as ex:Code* {
  ex:CodeList($value)
};
string-join(local:codes("ABC DEF"), ",")
