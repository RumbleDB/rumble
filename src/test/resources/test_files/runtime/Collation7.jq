(:JIQS: ShouldCrash; ErrorCode="XQST0038"; ErrorMetadata="LINE:5:COLUMN:28:" :)
declare default collation "http://www.w3.org/2005/xpath-functions/collation/codepoint";

for $i in ("foo", "bar")
group by $j := $i collation "http://www.example.com"
return $i
