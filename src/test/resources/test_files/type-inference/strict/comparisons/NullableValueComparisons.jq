(:JIQS: ShouldRun; Output="(24, true, true, false)" :)
(: Each union alternative yields one boolean, on either side of every value operator. :)
let $results := (
    for $x in (1, null)
    return (
        ($x eq 1) is statically xs:boolean,
        (1 eq $x) is statically xs:boolean,
        ($x ne 1) is statically xs:boolean,
        (1 ne $x) is statically xs:boolean,
        ($x lt 1) is statically xs:boolean,
        (1 lt $x) is statically xs:boolean,
        ($x le 1) is statically xs:boolean,
        (1 le $x) is statically xs:boolean,
        ($x gt 1) is statically xs:boolean,
        (1 gt $x) is statically xs:boolean,
        ($x ge 1) is statically xs:boolean,
        (1 ge $x) is statically xs:boolean
    )
) is statically xs:boolean+
return (
    count($results),
    every $result in $results satisfies $result instance of xs:boolean,
    $results[1],
    $results[13]
)
