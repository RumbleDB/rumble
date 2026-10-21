(:JIQS: ShouldRun; Output="(true, 2)" :)
import schema namespace l = "urn:library" at "Library.xsd";

(: l:library is the only global declaration, so it is the root of every strictly validated document. :)
let $library := validate {
    document { <l:library><l:book><l:title>A</l:title></l:book><l:book><l:title>B</l:title></l:book></l:library> }
}
return (
    exists($library/* is statically schema-element(l:library)),
    count($library/l:library/l:book/l:title is statically element(l:title, xs:string)*)
)
