(:JIQS: ShouldRun; Output="true" :)
let $items := fn:collection("nulls/")
let $object := subsequence($items, 2, 1)
let $array := subsequence($items, 5, 1)
return count($items) eq 6
    and subsequence($items, 1, 1) instance of null
    and $object.value instance of null
    and subsequence($items, 3, 1) eq 1
    and subsequence($items, 4, 1) instance of null
    and $array[[1]] instance of null
    and $array[[2]] eq 2
    and subsequence($items, 6, 1) eq 3
