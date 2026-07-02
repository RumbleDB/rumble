(:JIQS: ShouldRun; Output="(1, 10, 2, 20, 3, 30)" :)
fn:for-each(
  (1, 2, 3),
  function($x) { ($x, $x * 10) }
)
