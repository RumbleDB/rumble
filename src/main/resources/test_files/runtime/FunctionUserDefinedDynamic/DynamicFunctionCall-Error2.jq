(:JIQS: ShouldCrash; ErrorCode="XPTY0004";  ErrorMetadata="LINE:6:COLUMN:0:" :)
declare function fn1() {3};
declare function fn2() {5};
declare function fn3() {8};

(fn1#0, fn2#0, fn3#0)()

(: Dynamic function call can not be performed on a sequences :)
