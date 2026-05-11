jsoniq version "3.1";
(:JIQS: ShouldRun :)
(: Y cases in the primitive cast matrix :)
(
  3.0e0 cast as float,
  3.0e0 cast as double,
  12 cast as decimal,
  12 cast as integer,
  xs:date("2020-01-01") cast as dateTime,
  xs:dateTime("2020-01-01T10:00:00Z") cast as date,
  xs:yearMonthDuration("P2Y") cast as duration,
  xs:dayTimeDuration("P1D") cast as duration,
  true cast as double,
  false cast as integer,
  "0cd7" cast as hexBinary,
  "DNc=" cast as base64Binary,
  "2020-02-12" cast as date,
  "2015-05-03T13:20:00" cast as dateTime,
  "true" cast as boolean,
  "13" cast as integer,
  "13" cast as double,
  "P2Y" cast as yearMonthDuration,
  "P1D" cast as dayTimeDuration
) is statically xs:anyAtomicType+

