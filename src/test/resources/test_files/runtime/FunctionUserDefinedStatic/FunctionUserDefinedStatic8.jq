(:JIQS: ShouldRun; Output="false" :)
declare function udf2 ($i as integer) as boolean {
    udf3($i)
};

declare function udf3 ($i as integer) as boolean {
    5 < $i
};

udf2(3)

(: Functions calling each other :)
