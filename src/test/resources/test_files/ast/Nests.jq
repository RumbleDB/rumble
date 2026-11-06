(:JIQS: ShouldParse :)
for $var as integer in 1 to 10
let $j := 1 + 5 - 4 * 2 mod 7 or [1,2,3]
let $k := {"a": "b", "c":"d"}
let $z := $j + $k
group by $var
return $var