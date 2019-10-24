(:JIQS: ShouldRun; Output="10" :)
declare function fn1 ($n as integer) as integer { fn2($n) };
declare function fn2 ($n as integer) as integer { if ($n = 1) then 1 else $n + fn1($n - 1) };
fn1(4)

(: Forward Declaration & recursion :)
