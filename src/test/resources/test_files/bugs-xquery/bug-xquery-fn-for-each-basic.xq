(:JIQS: ShouldRun; Output="(2, 4, 6)" :)
fn:for-each(
  (1, 2, 3),
  function($x) { $x * 2 }
)
