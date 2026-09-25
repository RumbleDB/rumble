(:JIQS: ShouldRun; Output="(1, 10, 2, 20, 3, 30)" :)
fn:for-each-pair(
  (1, 2, 3, 4),
  (10, 20, 30),
  function($x, $y) { ($x, $y) }
)
