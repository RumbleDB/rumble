(:JIQS: ShouldRun; Output="true" :)
let $document := document {
  <root>
    <target xml:id="  target-id  "/>
  </root>
}
return
  string($document/root/target/@xml:id) eq "target-id"
  and data($document/root/target/@xml:id) instance of xs:ID
  and fn:id("target-id", $document) is $document/root/target
