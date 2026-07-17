(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $document := validate strict {
  document {
    <ex:identity-root>
      <ex:entry id="entry-one" refs="entry-two element-two">
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
  count(fn:idref("element-two", $document)) eq 2
  and $document/fn:idref("element-one") is $document/ex:identity-root/ex:entry[2]/@refs
  and fn:idref("entry-one", $document) is ($document//ex:reference)[2]
  and empty(fn:idref("invalid:name", $document))
