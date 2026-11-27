(:JIQS: ShouldRun; Output="true":)
declare function local:check($offset as xs:dayTimeDuration) as xs:string* {
  (
    try { string(adjust-time-to-timezone(xs:time("12:00:00Z"), $offset)) }
      catch * { local-name-from-QName($err:code) },
    try { string(adjust-date-to-timezone(xs:date("2000-01-01Z"), $offset)) }
      catch * { local-name-from-QName($err:code) },
    try { string(adjust-dateTime-to-timezone(xs:dateTime("2000-01-01T12:00:00Z"), $offset)) }
      catch * { local-name-from-QName($err:code) }
  )
};
(every $offset in ("PT1S", "-PT1S", "PT0.5S", "-PT0.5S", "PT14H1S", "-PT14H1S", "PT15H")
 satisfies deep-equal(local:check(xs:dayTimeDuration($offset)), ("FODT0003", "FODT0003", "FODT0003")))
and
(every $offset in ("PT14H", "-PT14H", "PT1M", "-PT1M", "PT0S")
 satisfies not(local:check(xs:dayTimeDuration($offset)) = "FODT0003"))
