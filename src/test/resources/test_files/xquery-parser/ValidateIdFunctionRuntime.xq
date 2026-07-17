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
  fn:id("entry-two", $document) is $document/ex:identity-root/ex:entry[2]
  and $document/fn:id("entry-one") is $document/ex:identity-root/ex:entry[1]
  and fn:id("invalid:name element-one", $document) is ($document//ex:identifier)[1]
  and count(fn:id("entry-one entry-two", $document)) eq 2
