(:JIQS: ShouldRun; Output="(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765)" :)
declare function f($i) {
  if($i le 2)
  then 1
  else f($i - 1) + f($i - 2)
};

for $j in 1 to 20 return f($j)
