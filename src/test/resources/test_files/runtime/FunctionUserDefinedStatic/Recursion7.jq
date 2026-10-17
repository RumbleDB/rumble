(:JIQS: ShouldRun; Output="(500000500000, 500000500000)" :)
declare function local:sum($n as integer, $total as integer) as integer {
  if ($n eq 0) then $total else local:sum($n - 1, $total + $n)
};

(local:sum(1000000, 0), local:sum#2(1000000, 0))

(: A tail recursion of depth one million, called statically and dynamically, does not overflow the stack. :)
