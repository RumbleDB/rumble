(:JIQS: ShouldCompile :)
for $var as integer in 1 to 10
let $j := let $internal as integer := $var return $internal
let $j := $var + $j - 10
let $arry as array := [1,2, "boo", 3.14, {"a":"b","c":"d"}, [1,2,3]]
let $j as string := "boooo"
return $var