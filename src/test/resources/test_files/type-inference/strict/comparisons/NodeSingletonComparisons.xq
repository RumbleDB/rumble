(:JIQS: ShouldRun; Output="(true, false, false)" :)
let $a := <a/>
return (
    ($a is $a) is statically xs:boolean,
    ($a << $a) is statically xs:boolean,
    ($a >> $a) is statically xs:boolean
)
