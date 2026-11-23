xquery version "3.1";
(:JIQS: ShouldRun; Output="" :)
(: compare() may return empty, so its declared result type must be optional. :)
let $v := compare((), "a")
return $v is statically xs:integer?
