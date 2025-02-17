(:JIQS: ShouldRun; Output="(true, false)" :)
declare function udf2 ($i as integer) as boolean {
    1 < $i
};

declare function udf3 ($i as integer) as boolean {
    5 < $i
};

udf2(3), udf3(3)
