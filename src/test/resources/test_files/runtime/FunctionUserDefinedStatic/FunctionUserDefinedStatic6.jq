(:JIQS: ShouldRun; Output="true" :)
declare function udf1 ($i as integer) {
    1 < $i
};
udf1(3)

(: No return type given :)
