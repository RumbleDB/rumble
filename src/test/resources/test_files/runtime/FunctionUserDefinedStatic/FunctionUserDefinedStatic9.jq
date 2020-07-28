(:JIQS: ShouldRun; Output="(4, 5, 5, 6)" :)
declare function udf1 () {
    (1, 2)
};

declare function udf2 () {
    (3, 4)
};

for $i in udf1()
for $j in udf2()
return $i + $j
