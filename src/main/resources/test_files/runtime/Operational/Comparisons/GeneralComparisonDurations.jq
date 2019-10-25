(:JIQS: ShouldRun; Output="(true, true, true, false, false, false, true)" :)
duration("P20M") = duration("P1Y8M"),
(duration("P20M"), duration("P1YT9999M")) = (duration("P1Y8M"), duration("P1Y6DT22H39M")),
(duration("P20M"), duration("P1YT9999M")) = (duration("P1Y8M")),
(duration("P20M"), duration("P1YT9999M")) = (duration("P1Y8MT0.001S")),
duration(()) != duration("PT0S"),
duration(()) = duration("PT0S"),
duration("P0Y0M0D") = duration("PT0S")

(: general tests :)