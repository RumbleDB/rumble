(:JIQS: ShouldRun; Output="(3, 4)" :)
let $identity := function($value as xs:integer) as xs:integer { $value }
for $x in (3, <a>4</a>)
return $identity($x) is statically xs:integer
