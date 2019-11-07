(:JIQS: ShouldRun; Output="(1, 2, 6, 24, 120)" :)
declare function factorial ($i as integer) as integer {
    if(($i = 0 or $i = 1))
    then 1
    else
    $i * factorial ($i - 1)
};

factorial(1),
factorial(2),
factorial(3),
factorial(4),
factorial(5)
