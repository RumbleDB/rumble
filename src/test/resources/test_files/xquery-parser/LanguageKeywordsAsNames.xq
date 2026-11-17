(:JIQS: ShouldRun; Output="3" :)
declare function local:xquery($jsoniq) { $jsoniq + 1 };
let $xquery := 2 return local:xquery($xquery)
