(:JIQS: ShouldRun; Output="(-P2Y3M, P12Y, P2Y6M5DT12H35M30S, P7DT5H18M13.300S, PT0S, PT4H6M5.500S)" :)
for $j as duration in (dayTimeDuration("P3DT99H66M4333.3S"), dayTimeDuration("P0DT0M"), dayTimeDuration("PT4H6M5.5S"), yearMonthDuration("-P2Y3M"), yearMonthDuration("P0Y0M"), yearMonthDuration(()), duration("P12Y"),duration("P144M"), duration("P2Y6M5DT12H35M30S"))
group by $j
order by string($j)
return $j
