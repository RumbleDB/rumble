(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, false, false, false, true, false, false, false)" :)
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
dayTimeDuration("PT0S") eq null,
yearMonthDuration("P0Y0M") gt yearMonthDuration("P0Y0M"),
dayTimeDuration("P0DT0H0M0S") gt dayTimeDuration("P0DT0H0M0S"),

let $dtdur := "P2DT3H" cast as dayTimeDuration
let $ymdur := "P1Y2M" cast as yearMonthDuration
return (
  $dtdur le $dtdur,
  $ymdur > $ymdur,
  $dtdur eq $ymdur
),
duration("P1Y") ne duration("P1Y")
