(:JIQS: ShouldCrash; ErrorCode="XPST0005" :)
(: The typed value of a parsed element is only known at runtime, unlike that of a constructed one. :)
declare function local:node($xml as xs:string) as element() { exactly-one(parse-xml($xml)/*) };
for $x in ('invalid', local:node("<a>4</a>"))
return $x + 1
