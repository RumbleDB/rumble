(:JIQS: ShouldRun; Output="(1, 1, 0, 0)" :)
for $present in (true(), false())
let $a := <a/>
let $b := if ($present) then $a else ()
return (
    count(($a is $b) is statically xs:boolean?),
    count(($b is $a) is statically xs:boolean?)
)
