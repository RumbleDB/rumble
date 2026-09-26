(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true, true)" :)
declare base-uri "http://example.org/tests/";

let $direct := <root/>
let $withBase := <root xml:base="http://example.com/base/"/>
let $nested := <root xml:base="http://example.com/base/"><child/></root>
let $nestedRel := <root xml:base="http://example.com/base/"><child xml:base="sub/"/></root>
let $nestedParent := <root xml:base="http://example.com/base/"><child xml:base="../sub/"/></root>
let $docElem := document { <root/> }
let $parsed := parse-xml("<root/>")
let $attr := <root xml:base="http://example.com/base/" attr="val"/>/@attr
let $iri := <root xml:base="http://example.com/café/"><child xml:base="résumé.xml"/></root>
return (
  fn:base-uri($direct) eq xs:anyURI("http://example.org/tests/"),
  fn:base-uri($withBase) eq xs:anyURI("http://example.com/base/"),
  fn:base-uri($nested/child) eq xs:anyURI("http://example.com/base/"),
  fn:base-uri($nestedRel/child) eq xs:anyURI("http://example.com/base/sub/"),
  fn:base-uri($nestedParent/child) eq xs:anyURI("http://example.com/sub/"),
  fn:base-uri($docElem) eq xs:anyURI("http://example.org/tests/"),
  fn:base-uri($parsed) eq xs:anyURI("http://example.org/tests/"),
  fn:base-uri($attr) eq xs:anyURI("http://example.com/base/"),
  fn:base-uri($iri/child) eq xs:anyURI("http://example.com/café/résumé.xml")
)
