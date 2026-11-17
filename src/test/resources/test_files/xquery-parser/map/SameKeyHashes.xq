(:JIQS: ShouldRun; Output="(1, 2, 3, 4, 5, 6)" :)
let $map := map:merge((
  map:entry(xs:dateTime("2000-01-01T12:00:00Z"), 1),
  map:entry(xs:time("12:00:00Z"), 2),
  map:entry(xs:yearMonthDuration("P1Y"), 3),
  map:entry(xs:dayTimeDuration("P1D"), 4),
  map:entry(xs:hexBinary("0A"), 5),
  map:entry(QName("http://example.com", "a:x"), 6)
))
return (
  $map(xs:dateTime("2000-01-01T13:00:00+01:00")),
  $map(xs:time("13:00:00+01:00")),
  $map(xs:duration("P12M")),
  $map(xs:duration("PT24H")),
  $map(xs:hexBinary("0a")),
  $map(QName("http://example.com", "b:x")),
  (: Without a timezone, the dateTime is not the same key. :)
  $map(xs:dateTime("2000-01-01T12:00:00"))
)
