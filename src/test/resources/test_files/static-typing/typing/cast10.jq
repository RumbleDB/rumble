jsoniq version "3.1";
(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: N cases in the primitive cast matrix :)
(
  3.0e0 cast as duration,
  3.0e0 cast as date,
  xs:date("2020-01-01") cast as integer,
  xs:time("10:00:00") cast as integer,
  xs:date("2020-01-01") cast as boolean
)

