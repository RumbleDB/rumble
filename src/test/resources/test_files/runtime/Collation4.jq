(:JIQS: ShouldRun; Output="(bar, foo)" :)
declare default collation "http://www.w3.org/2005/xpath-functions/collation/codepoint";

for $i in ("foo", "bar")
order by $i collation "http://www.w3.org/2005/xpath-functions/collation/codepoint"
return $i
