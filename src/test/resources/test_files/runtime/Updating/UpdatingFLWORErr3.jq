(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:6:COLUMN:4:" :)
copy $je := for $i in (1 to 4)
                 return [$i mod 2]
modify
    for $l in $je
    where replace value of json $l[[1]] with 1
    return
        replace value of json $l[[1]] with 1
return $je

(: expr in where clause is not simple :)