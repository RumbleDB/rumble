(:JIQS: ShouldCompile :)
declare function exitfunction($a, $b) {
    variable $c := $a + $b;
    exit returning $c;
};