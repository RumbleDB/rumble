(:JIQS: ShouldCompile :)
for $var as integer in 1 to 10
let $j := (let $internal := $var + 2
           return $internal)
return $var