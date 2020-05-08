(:JIQS: ShouldRun; Output="(true, true, true, false, false, false, true, true, true, true, false, false, true, true, true, true, false, true)" :)
duration("P20M") = duration("P1Y8M"),
(duration("P20M"), duration("P1YT9999M")) = (duration("P1Y8M"), duration("P1Y6DT22H39M")),
(duration("P20M"), duration("P1YT9999M")) = (duration("P1Y8M")),
(duration("P20M"), duration("P1YT9999M")) = (duration("P1Y8MT0.001S")),
duration(()) != duration("PT0S"),
duration(()) = duration("PT0S"),
duration("P0Y0M0D") = duration("PT0S"),
duration("P0Y0M0D") != null,
yearMonthDuration("P2Y2M") = yearMonthDuration("P26M"),
(yearMonthDuration("P20M"), yearMonthDuration("P1Y4M")) = (yearMonthDuration("P1Y8M"),yearMonthDuration("P1Y9M")),
(yearMonthDuration("P20M"), yearMonthDuration("P1Y9M")) = (yearMonthDuration("P1Y10M")),
yearMonthDuration(()) != yearMonthDuration("P0M"),
yearMonthDuration("P3Y3M") != null,
dayTimeDuration("P1DT25H") = dayTimeDuration("P2DT1H"),
(dayTimeDuration("PT4H6M5.5S"), dayTimeDuration("P1DT24H6M5.5S")) = (dayTimeDuration("P2DT6M5.5S"), dayTimeDuration("PT6H6M5.5S")),
(dayTimeDuration("PT4H6M5.5S"), dayTimeDuration("P1DT24H6M5.5S")) != (dayTimeDuration("P2DT6M5.5S")),
dayTimeDuration(()) != dayTimeDuration("PT0S"),
dayTimeDuration("PT0S") != null

(: general tests :)
