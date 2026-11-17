(:JIQS: ShouldRun; Output="true" :)
jsoniq version "3.1";

let $uri := "nulls/"
let $items := collection($uri)
return count($items) eq 4
    and map:contains($items[1], "value")
    and empty($items[1]?value)
    and $items[2] eq 1
    and array:size($items[3]) eq 2
    and empty($items[3]?1)
    and $items[3]?2 eq 2
    and $items[4] eq 3
    and count(uri-collection($uri)) eq 3
