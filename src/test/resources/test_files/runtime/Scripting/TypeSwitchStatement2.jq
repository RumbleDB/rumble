(:JIQS: ShouldRun; Output="correct" :)
variable $res;
typeswitch(duration("P4Y5MT5M"))
case string | decimal? | hexBinary* return $res := "not this";
case base64Binary+ return $res := "not even this";
default return $res := "correct";
$res

(: simple case with sequenceType union :)
