(:JIQS: ShouldRun; Output="true":)
(: F&O 3.1 §§9.3.1, 14.2.3: timezone equality is by value;
   PI content uses the collation, while node names and namespace URIs do not. :)
let $ci := "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive"
return every $ok in (
    dateTime(xs:date("2000-01-01-00:01"), xs:time("12:00:00-00:01"))
        eq xs:dateTime("2000-01-01T12:00:00-00:01"),
    timezone-from-dateTime(dateTime(xs:date("2000-01-01"), xs:time("12:00:00+00:01")))
        eq xs:dayTimeDuration("PT1M"),
    empty(dateTime((), xs:time("12:00:00"))),
    try { string(dateTime(xs:date("2000-01-01+00:01"), xs:time("12:00:00+00:02"))) eq "missing-error" }
        catch err:FORG0008 { true() },
    not(deep-equal(processing-instruction p {"A"}, processing-instruction p {"a"})),
    not(deep-equal(processing-instruction p {"A"}, processing-instruction P {"a"}, $ci)),
    deep-equal([processing-instruction p {"A"}], [processing-instruction p {"a"}], $ci),
    not(deep-equal(namespace p {"urn:A"}, namespace p {"urn:a"}, $ci))
) satisfies $ok
