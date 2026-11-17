(:JIQS: ShouldRun; Output="(true, true, true, true, true)" :)
xquery version "3.1";
declare base-uri "https://example.org/reference/";
declare decimal-format local:comma decimal-separator="," grouping-separator=".";
let $positions := (10, 20, 30) ! position#0
let $sizes := (10, 20, 30) ! last#0
let $format := function-lookup(QName("http://www.w3.org/2005/xpath-functions", "format-number"), 3)
let $base := static-base-uri#0
let $partial := (concat#2)(?, "suffix")
return (
    $positions[2]() eq 2,
    $sizes[1]() eq 3,
    $format(12.5, "0,0", "local:comma") eq "12,5",
    string($base()) eq "https://example.org/reference/",
    empty(function-name($partial)) and $partial("prefix") eq "prefixsuffix"
)
