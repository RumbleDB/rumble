(:JIQS: ShouldRun; Output="true":)
(: https://www.w3.org/TR/xquery-31/#axes :)
let $e := <root><before/><a x="1"><b><c/></b></a><after/></root>
return
  deep-equal($e/a/@x/following::* ! name(.), ("b", "c", "after"))
  and deep-equal($e/a/@x/preceding::* ! name(.), "before")
  and empty($e/a/@x/following-sibling::*)
  and empty($e/a/@x/preceding-sibling::*)
  and deep-equal($e/a/following::* ! name(.), "after")
  and deep-equal($e/a/preceding::* ! name(.), "before")
