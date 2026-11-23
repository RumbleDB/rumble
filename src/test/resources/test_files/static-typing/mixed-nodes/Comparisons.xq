(:JIQS: ShouldRun; Output="(true, true, true, false)" :)
(: The typed value of a parsed element is only known at runtime, unlike that of a constructed one. :)
declare function local:node($xml as xs:string) as element() { exactly-one(parse-xml($xml)/*) };
(: A value comparison compares untyped values as strings, a general comparison as the other operand's type. :)
(for $x in ('3', local:node("<a>3</a>")) return ($x eq '3') is statically xs:boolean?),
(for $x in (3, local:node("<a>4</a>")) return ($x = 3) is statically xs:boolean)
