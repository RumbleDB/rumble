(:JIQS: ShouldRun; Output="(-P2Y3M, PT0S, P0M, PT4H6M5.500S, P7DT5H18M13.3S, P2Y6M5DT12H35M30S, P12Y, P12Y)" :)
for $j as duration in (dayTimeDuration("P3DT99H66M4333.3S"), dayTimeDuration("P0DT0M"), dayTimeDuration("PT4H6M5.5S"), yearMonthDuration("-P2Y3M"), yearMonthDuration("P0Y0M"), yearMonthDuration(()), duration("P12Y"),duration("P144M"), duration("P2Y6M5DT12H35M30S"))
order by $j
return $j
