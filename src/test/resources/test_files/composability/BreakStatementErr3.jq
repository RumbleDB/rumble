(:JIQS: ShouldNotCompile; ErrorCode="SCCP0004"; ErrorMetadata="LINE:11:COLUMN:0:" :)
variable $je := for $i in (1 to 4)
       return [$i mod 2];
for $l in $je
    return {
        if($l[[1]] eq 0)
        then
            replace json value of $l[[1]] with 1;
        else
            replace json value of $l[[1]] with 0; }
break loop;