jsoniq version "3.1";
(:JIQS: ShouldCrash; ErrorCode="FORG0006" :)
if(({"a":2}, 3, "test", null))
then
"first branch"
else
"else branch"

(: arrays and objects, along with sequences where first item is array or object no longer have effective boolean value in jsoniq 3.1 :)