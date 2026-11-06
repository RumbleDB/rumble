(:JIQS: ShouldCrash; ErrorCode="SENR0001"; :)
declare function fn1() {3};
parallelize(fn1#0)

(: Named function ref - serialization error :)
