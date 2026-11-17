(:JIQS: ShouldRun; Output="(done, done)" :)
declare function local:count-down($n) {
  if ($n eq 0) then "done" else local:count-down($n - 1)
};

let $f := local:count-down#1
return ($f(3), (function($g) { $g(5) })($f))

(: A tail-recursive function called dynamically returns its result, not the function item of its tail call. :)
