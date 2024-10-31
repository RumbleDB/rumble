(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true, true)" :)
"P3Y8M" castable as duration,
yearMonthDuration("P2Y4M") castable as string,
yearMonthDuration("P2Y99M") castable as duration,
"-P6Y18M" castable as yearMonthDuration,
yearMonthDuration("P0Y0M") castable as dayTimeDuration,
dayTimeDuration("P3DT5H6.001S") castable as string,
dayTimeDuration("P3DT432H") castable as duration,
" -P4DT5M  " castable as dayTimeDuration,
dayTimeDuration("PT0S") castable as yearMonthDuration
