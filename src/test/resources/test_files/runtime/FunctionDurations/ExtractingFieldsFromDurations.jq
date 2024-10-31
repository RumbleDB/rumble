(:JIQS: ShouldRun; Output="(0, 1, 2, 3, 4, 5, 6, 7.89, 0, 0, -1)" :)
years-from-duration(duration("P0Y7MT5M30S")),
years-from-duration(duration("P1Y6M5DT12H35M30S")),
months-from-duration(yearMonthDuration("P9Y2M")),
days-from-duration(duration("P20M3D")),
hours-from-duration(duration("P20MT4H5M6S")),
minutes-from-duration(dayTimeDuration("PT3H5M")),
seconds-from-duration(dayTimeDuration("P0DT6M6S")),
seconds-from-duration(duration("PT7M7.890S")),
months-from-duration(dayTimeDuration("PT1M30.5S")),
minutes-from-duration(duration("P6DT60M")),
hours-from-duration(duration("-P6DT1H")),
days-from-duration(())

(: general tests :)
