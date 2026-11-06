(:JIQS: ShouldRun; Output="(true, false)" :)
declare function udf1 ($i as integer) as boolean {
    1 < $i
};
udf1(3), udf1(0)
