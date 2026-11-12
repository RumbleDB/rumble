(:JIQS: ShouldRun; Output="(early, early)" :)
declare function local:answer() {
    exit returning "early";
    42
};
let $anonymous := function() {
    exit returning "early";
    42
}
return (local:answer() is statically item*, $anonymous() is statically item*)
