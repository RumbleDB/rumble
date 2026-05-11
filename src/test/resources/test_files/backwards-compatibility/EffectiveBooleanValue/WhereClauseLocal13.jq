jsoniq version "1.0";
(:JIQS: ShouldRun; Output="1" :)
let $i := 1
where ({}, [], null)
return $i

(: arrays and objects, along with sequences where first item is array or object had effective boolean value true in jsoniq1.0 :)