jsoniq version "1.0";
(:JIQS: ShouldRun; Output="(true, true, true, true)" :)
boolean([1,2]),
boolean({"a":2}),
boolean([1,2]),
boolean(({"a":2}, 2))

(: arrays and objects, along with sequences where first item is array or object had effective boolean value true in jsoniq1.0 :)