(:JIQS: ShouldCrash; ErrorCode="XPST0017"; ErrorMetadata="LINE:2:COLUMN:17:" :)
let $raw-data := parallellize((
    {"id": 0, "category": "a", "categoryIndex": 1},
    {"id": 1, "category": "b", "categoryIndex": 2},
    {"id": 2, "category": "c", "categoryIndex": 3},
    {"id": 3, "category": "a", "categoryIndex": 1},
    {"id": 4, "category": "a", "categoryIndex": 1},
    {"id": 5, "category": "c", "categoryIndex": 3}
))
return $raw-data

(: typo on parallelize. Storing undefined function in a variable to test static context visitor multipass :)
