(:JIQS: ShouldRun; Output="true" :)
declare construction preserve;
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
document {
  validate strict { <ex:code-member>ABC</ex:code-member> }
} instance of document-node(schema-element(ex:code-head))
