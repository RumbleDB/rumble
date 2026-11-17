(:JIQS: ShouldRun; Output="false" :)
(: Binary values support ordering in Functions and Operators 3.1. :)
declare variable $base64b := "DNc=" cast as base64Binary;
$base64b lt $base64b
