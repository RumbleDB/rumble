(:JIQS: ShouldCompile :)
$je := for $i in (1 to 4)
       return [$i mod 2];
for $l in $je
    where replace json value of $l[[1]] with 1
    return
        {
            continue loop;
            replace json value of $l[[1]] with 1;
        }