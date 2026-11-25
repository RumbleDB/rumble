xquery version "3.1";
(:JIQS: ShouldRun; Output="<a>\n</a>\n" :)
(: A variable declared node() can hold an element; its self axis is not necessarily empty. :)
let $n as node() := <a/>
return $n/self::element()
