(:JIQS: ShouldRun; Output="({ "string10" : "2023-05-12", "string11" : "2023-05-12T12:34:56-07:00", "date" : "2023-05-12", "date2" : "2023-05-12Z", "dateTimeStamp" : "2023-05-12T12:34:56+02:00", "dateTimeStamp2" : "2023-05-12T00:00:00Z" })" :)
for $i in parallelize((1 to 2), 10)
let $string10 := string(date("2023-05-12"))
let $string11 := string(dateTimeStamp("2023-05-12T12:34:56-07:00"))
let $date := date("2023-05-12")
let $date2 := date(dateTimeStamp("2023-05-12T12:34:56Z"))
let $dateTimeStamp := dateTimeStamp("2023-05-12T12:34:56+02:00")
let $dateTimeStamp2 := dateTimeStamp($date)
return {
  "string10": $string10,
  "string11": $string11,
  "date" : $date,
  "date2" : $date2,
  "dateTimeStamp" : $dateTimeStamp,
  "dateTimeStamp2" : $dateTimeStamp2
}
