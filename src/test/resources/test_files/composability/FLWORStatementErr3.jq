(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:6:COLUMN:4:" :)
variable $je := for $i in (1 to 4)
                 return [$i mod 2];
variable $j := 1;
for $l in $je
    group by $j := replace json value of $l[[1]] with 1
    return
        replace json value of $l[[1]] with 1;


(: The expressions in the window-start, window-end, order-by, group-by, where and count clauses must be non-sequential. :)