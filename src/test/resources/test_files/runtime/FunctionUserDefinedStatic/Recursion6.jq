(:JIQS: ShouldRun; Output="(done, done, 15)" :)
declare function local:count-down($n, $x) {
  if ($n eq 0) then "done" else local:count-down($n - 1, $x)
};

declare function local:sum($acc, $n) {
  if ($n eq 0) then $acc else local:sum($acc + $n, $n - 1)
};

(for-each-pair((3, 5), (0, 0), local:count-down#2), fold-left((5), 0, local:sum#2))

(: A tail-recursive function called by a higher-order function returns its result. :)
