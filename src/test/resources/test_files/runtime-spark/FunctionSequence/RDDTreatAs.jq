(:JIQS: ShouldRun; Output="(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, P12Y, P2Y6M5DT12H35M30S, P13Y, P300D)" :)
parallelize(1 to 10) treat as decimal+,
parallelize((duration("P12Y"), duration("P2Y6M5DT12H35M30S"), yearMonthDuration("P13Y"), dayTimeDuration("P300D"))) treat as duration*
