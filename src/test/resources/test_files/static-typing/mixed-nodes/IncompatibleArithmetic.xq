(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: The typed value of a parsed element is only known at runtime, unlike that of a constructed one. :)
(: It may be a number, so the addition may succeed: the error is a possible type error, not XPST0005. :)
declare function local:node($xml as xs:string) as element() { exactly-one(parse-xml($xml)/*) };
for $x in ('invalid', local:node("<a>4</a>"))
return $x + 1
