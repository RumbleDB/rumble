(:JIQS: ShouldRun; Output="(3, 4)" :)
declare function local:identity($value as xs:integer) as xs:integer { $value };
for $x in (3, <a>4</a>)
return local:identity($x) is statically xs:integer
