(:JIQS: ShouldRun; Output="(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)" :)
declare function f($i) {
  if($i le 0)
  then 0
  else f($i - 1)
};

for $j in -10 to 10 return f($j)
