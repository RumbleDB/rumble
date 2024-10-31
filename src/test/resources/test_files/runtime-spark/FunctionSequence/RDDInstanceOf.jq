(:JIQS: ShouldRun; Output="(false, true, true, false)" :)
parallelize(1 to 10) instance of decimal,
parallelize(1 to 10) instance of decimal+,
parallelize((duration("P12Y"), duration("P2Y6M5DT12H35M30S"), yearMonthDuration("P13Y"), dayTimeDuration("P300D"))) instance of duration*,
parallelize(("hello", "world!", hexBinary("FA88"), base64Binary("0F+40A=="))) instance of string+
