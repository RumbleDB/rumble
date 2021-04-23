(:JIQS: ShouldCrash; ErrorCode="XPST0005"; ErrorMetadata="LINE:3:COLUMN:15:" :)
for $x in annotate({"foo":2},{"foo":"integer"})
group by $y := $x.foobar
return $x