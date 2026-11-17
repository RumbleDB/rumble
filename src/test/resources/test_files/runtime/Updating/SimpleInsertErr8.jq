(:JIQS: ShouldCrash; ErrorCode="XQDY0137"; ErrorMetadata="LINE:3:COLUMN:32:" :)
copy $je := {"a" : 1}
modify insert json "b" : 3 into {"a" : 1, "a" : 2}
return $je

(: content expr does not evaluate to object :)