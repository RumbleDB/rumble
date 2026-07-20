(:JIQS: ShouldRun; Output="5050 55" :)
declare function local:sum-tail($n as integer, $total as integer) as integer {
  if ($n = 0) then $total else local:sum-tail($n - 1, $total + $n)
};

for $n in (100, 10)
return local:sum-tail($n, 0)
