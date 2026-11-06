(:JIQS: ShouldRun; Output="(8, true)" :)
let $results := (
    for $x in (1, null)
    for $y in (xs:double(1), null)
    return (
        ($x eq $y) is statically xs:boolean,
        ($x lt $y) is statically xs:boolean
    )
) is statically xs:boolean+
return (count($results), every $result in $results satisfies $result instance of xs:boolean)
