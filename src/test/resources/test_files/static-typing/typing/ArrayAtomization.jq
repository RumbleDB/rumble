(:JIQS: ShouldRun; Output="(1, true, false, 2, true, 2, 1, 1, 2, 1)" :)
(
  ([1] cast as string) is statically string,
  [1] castable as integer,
  [1, 2] castable as integer,
  ([1] + 1) is statically integer?,
  [1, 2] = 2,
  index-of((1, 2), [2]),
  for $x in (1, 2) group by $k := [1] return $k,
  for $x in (2, 1) order by [$x] return $x
),
switch ([1]) case 1 return 1 default return 2
