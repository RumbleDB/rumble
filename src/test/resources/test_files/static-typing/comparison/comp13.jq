(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare variable $hexb := "0cd7" cast as hexBinary;
declare variable $base64b := "DNc=" cast as base64Binary;
$hexb eq $base64b