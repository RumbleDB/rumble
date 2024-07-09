(:JIQS: ShouldNotCompile; ErrorCode: RBST0004; :)
declare function exitfunction($a, $b) {
    variable $c := {};
    exit returning insert json {"foo": 1} into $c;
};