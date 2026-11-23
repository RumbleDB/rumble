xquery version "3.1";
(:JIQS: ShouldCrash; ErrorCode="XPST0005" :)
(: Empty atomized content constructs no text node. Inferring that empty type must raise XPST0005,
   even though the later comma expression would produce a nonempty result at runtime. :)
declare function local:id($n as node()) as node() { $n };

let $t := text { () }
let $v := ($t, <a/>)
return local:id($v)
