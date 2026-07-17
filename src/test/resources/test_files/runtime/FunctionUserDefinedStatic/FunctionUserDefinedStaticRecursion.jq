(:JIQS: ShouldRun; Output="6 1 15 3 5050 10" :)
declare function local:sum-down($n as integer) as integer {
  if ($n = 0) then 0 else $n + local:sum-down($n - 1)
};

declare function local:sum-tail($n as integer, $total as integer) as integer {
  if ($n = 0) then $total else local:sum-tail($n - 1, $total + $n)
};

(
  for $n in (3, 1, 5, 2)
  return local:sum-down($n),
  local:sum-tail(100, 0),
  local:sum-tail(4, 0)
)
