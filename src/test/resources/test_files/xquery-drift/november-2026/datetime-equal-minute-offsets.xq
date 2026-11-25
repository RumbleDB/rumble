(:JIQS: ShouldRun; Output="true":)
(: F&O 3.1 §9.3.1 permits equal explicit timezones on the two arguments.
   https://www.w3.org/TR/xpath-functions-31/#func-dateTime
   Observed on master c6cbdb7fa: FORG0008 instead of true.
   A one-minute offset exposes reference comparison of Java ZoneOffset objects. :)
dateTime(xs:date("2000-01-01+00:01"), xs:time("00:00:00+00:01"))
    eq xs:dateTime("2000-01-01T00:00:00+00:01")
