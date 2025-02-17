(:JIQS: ShouldRun; Output="{ "b" : 1 }" :)
variable $je;
let $a := {"a" : 1}
return
    {
        $je := $a;
        rename json $je.a as "b";
    }
$je