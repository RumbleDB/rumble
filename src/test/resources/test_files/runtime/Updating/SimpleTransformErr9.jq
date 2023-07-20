(:JIQS: ShouldCrash; ErrorCode="XUDY0014"; ErrorMetadata="LINE:5:COLUMN:11:" :)
let $data := delta-file("../../../queries/sample_json_delta")
return (
    copy $je := {"a" : 1}
    modify delete $data.string
    return $je
)
(: Attempt to modify mutable delta file value inside transform without copy :)
