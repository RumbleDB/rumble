(:JIQS: ShouldRun; Output="(24, true, true, false)" :)
(: Each union alternative yields one boolean, on either side of every general operator. :)
let $results := (
    for $x in (1, null)
    return (
        ($x = 1) is statically xs:boolean,
        (1 = $x) is statically xs:boolean,
        ($x != 1) is statically xs:boolean,
        (1 != $x) is statically xs:boolean,
        ($x < 1) is statically xs:boolean,
        (1 < $x) is statically xs:boolean,
        ($x <= 1) is statically xs:boolean,
        (1 <= $x) is statically xs:boolean,
        ($x > 1) is statically xs:boolean,
        (1 > $x) is statically xs:boolean,
        ($x >= 1) is statically xs:boolean,
        (1 >= $x) is statically xs:boolean
    )
) is statically xs:boolean+
return (
    count($results),
    every $result in $results satisfies $result instance of xs:boolean,
    $results[1],
    $results[13]
)
