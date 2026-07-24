(:JIQS: ShouldRun; Output="true" :)
declare function local:element() as element() {
  <element xmlns="urn:test">value</element>
};
string(local:element()) eq "value"
