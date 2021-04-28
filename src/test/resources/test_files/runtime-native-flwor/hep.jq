(:JIQS: ShouldParse :)
module namespace hep = "hep.jq";

declare function hep:histogram($values, $lo, $hi, $num-bins) {
  let $width := float(($hi - $lo) div $num-bins)
  let $half-width := float($width div 2)

  let $underflow := float(round(($lo - $half-width) div $width))
  let $overflow := float(round(($hi - $half-width) div $width))
  return
  for $v in $values
  let $bucket-idx :=
    if ($v lt $lo) then exactly-one($underflow)
    else
      if ($v gt $hi) then exactly-one($overflow)
      else round(($v - $half-width) div $width)
  let $center := $bucket-idx * $width + $half-width

  group by $center
  order by $center
  return {"x": $center, "y": count($v)}
};