(:JIQS: ShouldRun; Output="true":)
(: https://www.w3.org/TR/xquery-31/#axes :)
(: The parent's children do not precede its attributes. :)
let $e := <a x="1"><b/><c/></a>
return empty($e/@x/preceding::*)
