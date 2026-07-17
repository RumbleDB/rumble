(:JIQS: ShouldCrash; ErrorCode="XQST0034" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
declare function ex:Code($value as xs:string) as xs:string {
  $value
};
ex:Code("ABC")
