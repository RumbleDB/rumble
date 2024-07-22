(:JIQS: ShouldCrash; UpdateDim=[4,2]; ErrorCode="XUDY0014"; ErrorMetadata="LINE:5:COLUMN:11:" :)
let $data := delta-file("./tempDeltaTable")
return (
    copy $je := {"a" : 1}
    modify delete $data.string
    return $je
)
(: Attempt to modify mutable delta file value inside transform without copy :)
