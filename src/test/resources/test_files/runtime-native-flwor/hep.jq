(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace hep = "hep.jq";

declare function hep:histogram($values, $lo, $hi, $num-bins) {
  let $flo := float($lo)
  let $fhi := float($hi)
  let $width := ($fhi - $flo) div float($num-bins)
  let $half-width := $width div 2
  let $offset := $flo mod $half-width

  return
    for $value in $values
    let $truncated-value :=
      if ($value lt $flo) then $flo - $half-width
      else
        if ($value gt $fhi) then $fhi + $half-width
        else $value - $offset
    let $bucket-idx := floor($truncated-value div $width)
    let $center := $bucket-idx * $width + $half-width + $offset

    group by $center
    order by $center
    return {"x": $center, "y": count($value)}
};