(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $root := validate strict {
  <ex:identity-root>
    <ex:entry id="entry-one" refs="entry-one element-two">
      <ex:identifier>element-one</ex:identifier>
      <ex:reference>element-two</ex:reference>
    </ex:entry>
    <ex:entry id="entry-two" refs="entry-one element-one">
      <ex:identifier>element-two</ex:identifier>
      <ex:reference>element-one</ex:reference>
    </ex:entry>
  </ex:identity-root>
}
return
  data($root/ex:entry[1]/@id) instance of xs:ID
  and (every $reference in data($root/ex:entry[1]/@refs)
      satisfies $reference instance of xs:IDREF)
  and data($root/ex:entry[1]/ex:identifier) instance of xs:ID
  and data($root/ex:entry[1]/ex:reference) instance of xs:IDREF
