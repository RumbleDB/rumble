(:JIQS: ShouldRun; Output="true" :)
let $doc := document {<div/>} return $doc ! (count(/div) eq 1)
