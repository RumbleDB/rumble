(:JIQS: ShouldRun; Output="(11, 22, 33)" :)
fn:for-each-pair(
  (1, 2, 3),
  (10, 20, 30, 40),
  function($x, $y) { $x + $y }
)
