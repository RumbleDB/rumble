(:JIQS: ShouldRun; Output="true":)
(: https://www.w3.org/TR/xquery-31/#axes :)
(: Attributes precede their parent's children in document order. :)
let $e := <a x="1"><b/><c/></a>
return deep-equal($e/@x/following::* ! name(.), ("b", "c"))
