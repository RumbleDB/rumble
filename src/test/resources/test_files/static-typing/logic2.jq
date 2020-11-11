(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare $date := '2020-02-12' cast as date
declare $dt := "2015-05-03T13:20:00" cast as dateTime
declare $time := "13:20:00" cast as time
declare $hexb := "0cd7" cast as hexBinary
declare $base64b := "DNc=" cast as base64Binary
$date or $time or $dt or $hexb and $base64b