(:JIQS: ShouldRun; Output="first branch" :)
if(({"a":2}, 3, "test", null))
then
"first branch"
else
"else branch"

(: arrays and objects, along with sequences where first item is array or object had effective boolean value true in jsoniq1.0 :)