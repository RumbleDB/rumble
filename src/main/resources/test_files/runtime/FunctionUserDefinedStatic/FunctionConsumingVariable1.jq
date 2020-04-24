(:JIQS: ShouldRun; Output="(true, false)" :)
declare variable $x as integer := 1; 
declare function udf1 ($i as integer) as boolean {
    $x < $i
};
udf1(3), udf1(0)
