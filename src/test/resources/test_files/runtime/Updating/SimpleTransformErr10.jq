(:JIQS: ShouldCrash; ErrorCode="XUDY0014"; ErrorMetadata="LINE:10:COLUMN:27:" :)
copy $je := [1 to 4]
modify ()
return (
    let $x := (
        copy $ej := [5 to 8]
        modify (
            let $x := (
                    copy $ee := [5 to 8]
                    modify delete $je[[1]]
                    return $ee
                )
            return delete $ej[[2]]
        )
        return $ej
    )
    return $je
)

(: target of modify is not same level copy var :)