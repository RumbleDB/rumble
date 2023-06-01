(:JIQS: ShouldCrash; ErrorCode="XUDY0014"; ErrorMetadata="LINE:6:COLUMN:15:" :)
copy $je := [1 to 4]
modify (
    let $x := (
        copy $ej := [5 to 8]
        modify delete $je[[1]]
        return $ej
    )
    return delete $je[[2]]
)
return $je

(: target of modify is not same level copy var :)
