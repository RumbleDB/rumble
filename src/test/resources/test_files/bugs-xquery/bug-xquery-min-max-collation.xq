(:JIQS: ShouldRun; Output="true" :)
let $ascii := "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive",
    $uca := "http://www.w3.org/2013/collation/UCA?lang=en;strength=primary"
return
    max(("a", "B"), $ascii) eq "B"
    and min(("a", "B"), $ascii) eq "a"
    and max(("é", "f"), $uca) eq "f"
    and min(("é", "f"), $uca) eq "é"
    and string(max((xs:anyURI("a"), xs:anyURI("B")), $ascii)) eq "B"
    and max((2, 10), $ascii) eq 10
