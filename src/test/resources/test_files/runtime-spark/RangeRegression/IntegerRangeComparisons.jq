(:JIQS: ShouldRun; Output="true" :)
(: Compare the endpoint shortcut with existential value comparisons over small ranges.
   Cover a multi-item range, a singleton, and an empty range in both operand orders. :)
empty(
  for $a in (0, 2, 3)
  let $b := if ($a eq 0) then 3 else 2
  for $x in (-1, 0, 1, 2, 3, 4)
  where not(
    (($x = ($a to $b)) eq (some $y in $a to $b satisfies $x eq $y))
    and (($a to $b) = $x) eq (some $y in $a to $b satisfies $y eq $x)
    and (($x != ($a to $b)) eq (some $y in $a to $b satisfies $x ne $y))
    and (($a to $b) != $x) eq (some $y in $a to $b satisfies $y ne $x)
    and (($x < ($a to $b)) eq (some $y in $a to $b satisfies $x lt $y))
    and (($a to $b) < $x) eq (some $y in $a to $b satisfies $y lt $x)
    and (($x <= ($a to $b)) eq (some $y in $a to $b satisfies $x le $y))
    and (($a to $b) <= $x) eq (some $y in $a to $b satisfies $y le $x)
    and (($x > ($a to $b)) eq (some $y in $a to $b satisfies $x gt $y))
    and (($a to $b) > $x) eq (some $y in $a to $b satisfies $y gt $x)
    and (($x >= ($a to $b)) eq (some $y in $a to $b satisfies $x ge $y))
    and (($a to $b) >= $x) eq (some $y in $a to $b satisfies $y ge $x)
  )
  return $x
)
and (2.5 < (1 to 3))
and not(2.5 = (1 to 3))
and not(() = (1 to 3))

(: QT3 RangeExpr-409c/d and the negative-integer family must not scan the full range. :)
and (1000000000000000020001 < (1000000000000000000000 to 1000000000000500000003))
and (1000000000000000020001 = (1000000000000000000000 to 1000000000000010000003))
and (-1000000000000000000003 = (-1000000000000000000003 to -1000000000000000000000))
and count(1000000000000000000000 to 1000000000000500000003) eq 500000004
and count(subsequence(1000000000000000000000 to 1000000000000500000003, 3, 5)) eq 5
