(:JIQS: ShouldRun :)
declare variable $date := "2020-02-12" cast as date;
declare variable $dt := "2015-05-03T13:20:00" cast as dateTime;
declare variable $hexb := "0cd7" cast as hexBinary;
declare variable $base64b := "DNc=" cast as base64Binary;
($base64b cast as hexBinary) is statically hexBinary, ($hexb cast as base64Binary) is statically base64Binary, ($dt cast as date) is statically date, ($dt cast as time) is statically time, ($date cast as dateTime) is statically dateTime, (null cast as null) is statically null, (3 cast as integer?) is statically integer