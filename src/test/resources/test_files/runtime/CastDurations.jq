(:JIQS: ShouldRun; Output="(P3Y5M, P2Y4M, P10Y3M, P3DT5H6.001S, P21D)" :)
duration("P3Y5M") cast as string,
yearMonthDuration("P2Y4M") cast as string,
yearMonthDuration("P2Y99M") cast as duration,
dayTimeDuration("P3DT5H6.001S") cast as string,
dayTimeDuration("P3DT432H") cast as duration
