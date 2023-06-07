(:JIQS: ShouldCrash; ErrorCode="RBDY0007"; ErrorMetadata="LINE:4:COLUMN:17:" :)
let $x := [1 to 4]
let $y := copy $je := [1 to 4]
          modify delete $x[[1]]
          return $je
return $x

(: target of modify is not mutable :)
