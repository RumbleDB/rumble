(:JIQS: ShouldCrash; ErrorCode="XPST0017"; ErrorMetadata="LINE:3:COLUMN:10:":)
let $f := function($x) { $x.guess eq "English" }
for $o in json-lines("... language test ...")
where $f($o)
return $o

(: referencing non-existing function in a FLWOR clause :)
