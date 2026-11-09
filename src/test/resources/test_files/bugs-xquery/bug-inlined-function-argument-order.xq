(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:4:COLUMN:8:" :)
(: An inlined call converts its arguments in order, so it reports the first invalid one, like the call. :)
declare function local:f($a as xs:integer, $b as xs:integer) { $a + $b };
local:f("x", "y")
