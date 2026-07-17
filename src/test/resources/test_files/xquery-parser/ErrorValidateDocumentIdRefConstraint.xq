(:JIQS: ShouldCrash; ErrorCode="XQDY0027" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
validate strict {
  document {
    <ex:identity-root>
      <ex:entry id="entry-one" refs="missing-id">
        <ex:identifier>element-one</ex:identifier>
        <ex:reference>another-missing-id</ex:reference>
      </ex:entry>
    </ex:identity-root>
  }
}
