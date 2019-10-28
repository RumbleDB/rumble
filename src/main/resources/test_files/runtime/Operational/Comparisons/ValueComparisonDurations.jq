(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, false)" :)
duration("P20M") eq duration("P1Y8M"),
duration(()) ne duration("PT0S"),
duration(()) eq duration("PT0S"),
duration("P0Y0M0D") eq duration("PT0S"),
duration("P0Y0M0D") ne null,
yearMonthDuration("P2Y2M") eq yearMonthDuration("P26M"),
yearMonthDuration(()) ne yearMonthDuration("P0M"),
yearMonthDuration("P3Y3M") ne null,
dayTimeDuration("P1DT25H") eq dayTimeDuration("P2DT1H"),
dayTimeDuration(()) ne dayTimeDuration("PT0S"),
dayTimeDuration("PT0S") eq null

(: general tests :)