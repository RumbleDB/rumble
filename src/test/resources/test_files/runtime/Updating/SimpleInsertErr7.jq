(:JIQS: ShouldCrash; ErrorCode="JNUP0019"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := {"a" : 1}
modify insert json false into $je
return $je

(: content expr does not evaluate to object :)