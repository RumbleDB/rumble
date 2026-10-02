(:JIQS: ShouldRun; Output="(1, 2, 1, 2, 1, 2, 1, 2)" :)
declare variable $i := local:f();
declare function local:f() {
    (for $i in (1, 2) return $i),
    (let $i := (1, 2) return $i),
    (for $x at $i in (10, 20) return $i),
    (for $x in (10, 20) count $i return $i)
};

$i
