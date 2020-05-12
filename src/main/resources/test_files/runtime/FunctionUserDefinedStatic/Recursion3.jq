(:JIQS: ShouldRun; Output="(1, 1, 2, 3, 5, 8, 13, 21, 34, 55)" :)
declare function f($i) {
  if($i le 2)
  then 1
  else f($i - 1) + f($i - 2)
};

for $j in 1 to 10 return f($j)
