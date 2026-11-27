(:JIQS: ShouldRun; Output="true":)
xquery version "3.1";

let $result :=
    for $item in parallelize((3, 2, 1), 2)
    order by ([()], [if ($item eq 2) then () else $item]) empty greatest
    return $item
return deep-equal($result, (1, 3, 2))
