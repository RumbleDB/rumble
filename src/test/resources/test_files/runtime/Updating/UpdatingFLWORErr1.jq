(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:4:COLUMN:8:" :)
copy json $je := { "a" : {"foo" : 1}}
modify
    let $l := rename json $je.a.foo as "b"
    return
        rename json $l.foo as "b"
return $je

(: expr in let clause is not simple :)