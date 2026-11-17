(:JIQS: ShouldRun; Output="(true, true)" :)
declare function local:sum($n as integer, $total as integer) as double {
  if ($n eq 0) then $total else local:sum($n - 1, $total + $n)
};

let $sum := local:sum(1000, 0)
return ($sum eq 500500, $sum instance of double)

(: A tail-recursive function with declared types: the result of the last tail call is promoted to double. :)
