(:JIQS: ShouldRun; Output="(foo, bar)" :)
declare default collation "http://www.w3.org/2005/xpath-functions/collation/codepoint";

for $i in ("foo", "bar")
group by $j := $i collation "http://www.w3.org/2005/xpath-functions/collation/codepoint"
return $i
