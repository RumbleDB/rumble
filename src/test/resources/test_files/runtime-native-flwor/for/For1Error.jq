(:JIQS: ShouldCrash; ErrorCode="XPST0005"; ErrorMetadata="LINE:3:COLUMN:10:" :)
for $x in annotate({"foo":2},{"foo":"integer"})
for $y in $x.foobar[]
return $x