(:JIQS: ShouldRun; Output="foobar" :)
let $o := {"foobar": 2, "1" : "bar"} return ("foo", "bar", "foobar")[$o.$$ eq 2]

(: context expression with filter :)
