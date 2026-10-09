(:JIQS: ShouldRun; Output="(2, 2, 3, 1, 1)" :)
import schema namespace d = "urn:sections" at "Sections.xsd";

let $doc := validate {
    <d:doc><d:section level="1"><d:title>A</d:title><d:section level="2"><d:title>B</d:title></d:section></d:section></d:doc>
}
return (
    count($doc//d:title is statically element(d:title, xs:string)*),
    count($doc/descendant::d:section is statically element(d:section, d:Section)*),
    sum($doc//@level is statically attribute(level, xs:integer)*),
    (: The step after // keeps its schema type through a predicate. :)
    count($doc//d:section[@level = 1] is statically element(d:section, d:Section)*),
    (: The wildcard allows elements that the schema does not describe. :)
    count((validate { <d:open><d:title>C</d:title></d:open> })//d:title is statically element(d:title)*)
)
