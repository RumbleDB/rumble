(:JIQS: ShouldRun; Output="false" :)
(: Binary values support ordering in Functions and Operators 3.1. :)
declare variable $hexb := "0cd7" cast as hexBinary;
$hexb lt $hexb
