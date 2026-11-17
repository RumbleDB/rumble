(:JIQS: ShouldRun; Output="true" :)
let $items := fn:collection("mixed/")
let $xml := subsequence($items, 1, 1)
let $json := subsequence($items, 2, 1)
let $array := subsequence($items, 4, 1)
return count($items) eq 5
    and $xml instance of document-node()
    and string($xml) eq "7"
    and $json.value eq 8
    and subsequence($items, 3, 1) eq 9
    and $array instance of array
    and $array[[2]] eq 11
    and subsequence($items, 5, 1) eq "last"
