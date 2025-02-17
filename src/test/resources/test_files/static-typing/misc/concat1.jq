(:JIQS: ShouldRun :)
declare variable $date := "2020-02-12" cast as date;
declare variable $hexb := "0cd7" cast as hexBinary;
("qwe" || "rty") is statically string, (() || ()) is statically string, (12 || $date) is statically string, (33.3 || $hexb) is statically string, (12 treat as atomic? || null) is statically string