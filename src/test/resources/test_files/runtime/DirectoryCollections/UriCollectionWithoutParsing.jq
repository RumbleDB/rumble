(:JIQS: ShouldRun; Output="true" :)
let $uris := fn:uri-collection("unparseable/")
return count($uris) eq 1 and ends-with(string($uris), "/unparseable/bad.json")
