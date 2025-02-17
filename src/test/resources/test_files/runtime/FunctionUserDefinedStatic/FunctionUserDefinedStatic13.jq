(:JIQS: ShouldRun; Output="(1, 2, 3)" :)
declare function udf1 ($i as integer) as integer* {
    udf2($i)
};

declare function udf2 ($i as integer) as integer* {
    udf3($i)
};

declare function udf3 ($i as integer) as integer* {
    parallelize(1 to $i)
};

udf1(3)

(: Functions calling each other - RDD body - 3 levels:)
