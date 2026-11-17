(:JIQS: ShouldRun; Output="true" :)
let $values := parallelize(("a", "B")),
    $collation := "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive"
return max($values, $collation) eq "B" and min($values, $collation) eq "a"
