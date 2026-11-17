(:JIQS: ShouldRun; Output="true" :)
let $uris := fn:uri-collection("mixed/")
return count($uris) eq 3
    and ends-with(string(subsequence($uris, 1, 1)), "/mixed/a.xml")
    and ends-with(string(subsequence($uris, 2, 1)), "/mixed/b.json")
    and ends-with(string(subsequence($uris, 3, 1)), "/mixed/c.jsonl")
