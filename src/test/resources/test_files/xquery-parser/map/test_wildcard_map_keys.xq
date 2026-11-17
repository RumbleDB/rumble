(:JIQS: ShouldRun; Output="(true, true)" :)
declare namespace a = "http://example.com";

let $m28 := (
  <dot>
    <a:b>key</a:b>
    <c>value</c>
  </dot>
  !
  map{a:*:c}
)
let $m31 := (
  <dot>
    <a:b>key</a:b>
  </dot>
  !
  map{a:*:*}
)
return (
  deep-equal($m28, map{"key":<c>value</c>}),
  deep-equal($m31, map{"key":<a:b xmlns:a="http://example.com">key</a:b>})
)
