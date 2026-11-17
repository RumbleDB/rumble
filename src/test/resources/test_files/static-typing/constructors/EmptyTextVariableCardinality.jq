xquery version "3.1";
(:JIQS: ShouldRun; Output="<a>\n</a>\n" :)
(: Empty atomized content constructs no text node, leaving exactly one node in $v. :)
declare function local:id($n as node()) as node() { $n };

let $t := text { () }
let $v := ($t, <a/>)
return local:id($v)
