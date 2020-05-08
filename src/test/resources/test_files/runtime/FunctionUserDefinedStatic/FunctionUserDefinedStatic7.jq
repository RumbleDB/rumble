(:JIQS: ShouldRun; Output="true" :)
declare function udf1 ($i) as boolean {
    1 < $i
};
udf1(3)

(: No param type given :)
