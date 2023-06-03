(:JIQS: ShouldCrash; ErrorCode="NeedToRename"; ErrorMetadata="LINE:5:COLUMN:11:" :)
let $data := delta-file("../../../queries/sample_updating_delta")
return (
    copy $je := {"a" : 1}
    modify delete $data.bool
    return $je
)
(: Attempt to modify mutable delta file value inside transform without copy :)
