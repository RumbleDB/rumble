(:JIQS: ShouldRun; Output="true" :)
let $doc := document {<r/>} return $doc ! (count((/)/*) eq 1)
