(:JIQS: ShouldRun; Output="early" :)
declare function local:answer() {
    exit returning "early";
    42
};
local:answer() is statically item*
