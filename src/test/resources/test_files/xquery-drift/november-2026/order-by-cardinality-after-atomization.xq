(:JIQS: ShouldRun; Output="true":)
(: Each pair of arrays atomizes to a single ordering key.
   https://www.w3.org/TR/xquery-31/#id-order-by :)
let $sorted :=
    for $x in (2, 1)
    order by ([()], [$x])
    return $x
return deep-equal($sorted, (1, 2))
