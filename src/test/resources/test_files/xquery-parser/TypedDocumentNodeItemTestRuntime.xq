(:JIQS: ShouldRun; Output="true" :)
declare construction preserve;
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
document {
  validate strict { <ex:nil-root>ABC</ex:nil-root> }
} instance of document-node(element(ex:nil-root, ex:CodeContent))
