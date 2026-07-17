(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
(validate strict {
  <ex:identity-root>
    <ex:entry id="entry-one" refs="missing-id">
      <ex:identifier>element-one</ex:identifier>
      <ex:reference>another-missing-id</ex:reference>
    </ex:entry>
  </ex:identity-root>
}) instance of element()
