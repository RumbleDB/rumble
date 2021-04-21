(:JIQS: ShouldCrash; ErrorCode="XPST0005"; ErrorMetadata="LINE:3:COLUMN:10:" :)
for $x in annotate({"foo":[ 2, 3 ] },{"foo": [ "integer" ]})
let $y := $x.foo.foobar
return $y
