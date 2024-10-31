(:JIQS: ShouldRun; Output="(true, true, true, true)" :)
boolean([1,2]),
boolean({"a":2}),
boolean([1,2]),
boolean(({"a":2}, 2))

(: first item array and objects are evaluated to true :)
