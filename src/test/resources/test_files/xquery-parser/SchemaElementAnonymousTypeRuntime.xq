(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
validate strict {
  <ex:anonymous-root><ex:value>content</ex:value></ex:anonymous-root>
} instance of schema-element(ex:anonymous-root)
