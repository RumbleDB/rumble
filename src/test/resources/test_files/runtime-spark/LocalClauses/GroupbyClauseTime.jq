(:JIQS: ShouldRun; Output="(00:00:00, 13:20:00-05:00, 13:20:00, 13:20:30.555, 12:12:12Z)" :)
for $j as time in (time("12:12:12Z"), time("13:20:00"), time("13:20:30.5555"), time("13:20:00-05:00"), time("13:20:00Z"), time("00:00:00"), time("24:00:00"), time(()))
group by $j
return $j
