(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare variable $date := "2020-02-12" cast as date;
declare variable $dt := "2015-05-03T13:20:00" cast as dateTime;
declare variable $time := "13:20:00" cast as time;
declare variable $hexb := "0cd7" cast as hexBinary;
declare variable $base64b := "DNc=" cast as base64Binary;
$date or $time or $dt or $hexb and $base64b