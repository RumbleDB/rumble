(:JIQS: ShouldCrash; ErrorCode="XQST0038"; ErrorMetadata="LINE:2:COLUMN:16:" :)
declare default collation "http://www.w3.org/2005/xpath-functions/collation/codepoint";

for $i in ("foo", "bar")
order by $i collation "http://www.example.com"
return $i
