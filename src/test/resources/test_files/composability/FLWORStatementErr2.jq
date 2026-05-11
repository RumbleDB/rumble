(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:6:COLUMN:4:" :)
variable $je := for $i in (1 to 4)
                 return [$i mod 2];

for $l in $je
    where replace value of json $l[[1]] with 1
    return
        replace value of json $l[[1]] with 1;


(: The expressions in the order-by, group-by and where clauses must be non-updating. :)