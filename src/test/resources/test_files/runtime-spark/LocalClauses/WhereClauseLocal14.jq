jsoniq version "3.1";
(:JIQS: ShouldCrash; ErrorCode="FORG0006" :)
let $i := 1
where ([], {}, null)
return $i
(: effective boolean value of objects and arrays undefined as of jsoniq3.1 :)
