(:JIQS: ShouldCompile :)
variable $je := for $i in (1 to 4)
       return [$i mod 2];
for $l in $je
    where replace value of json $l[[1]] with 1
    return
        {
            continue loop;
            replace value of json $l[[1]] with 1;
        }