(:JIQS: ShouldRun; Output="(1, 2, 3)" :)
declare function udf2 ($i as integer) as integer* {
    udf3($i)
};

declare function udf3 ($i as integer) as integer* {
    parallelize(1 to $i)
};

udf2(3)

(: Functions calling each other - RDD body :)
