(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $document := validate strict {
  document {
    <ex:identity-root>
      <ex:entry id="entry-one" refs="entry-two">
        <ex:identifier>element-one</ex:identifier>
        <ex:reference>element-two</ex:reference>
      </ex:entry>
      <ex:entry id="entry-two" refs="element-one">
        <ex:identifier>element-two</ex:identifier>
        <ex:reference>entry-one</ex:reference>
      </ex:entry>
    </ex:identity-root>
  }
}
return
  fn:element-with-id("entry-one", $document) is $document/ex:identity-root/ex:entry[1]
  and fn:element-with-id("element-two", $document) is $document/ex:identity-root/ex:entry[2]
