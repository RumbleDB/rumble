(:JIQS: ShouldRun; Output="(3, 3, 3, true, 4, 4, 4, true)" :)
(: The typed value of a parsed element is only known at runtime, unlike that of a constructed one. :)
declare function local:node($xml as xs:string) as element() { exactly-one(parse-xml($xml)/*) };
for $x in ('3', local:node("<a>4</a>"))
return (
    ($x cast as xs:integer) is statically xs:integer,
    ($x cast as xs:integer?) is statically xs:integer?,
    xs:integer($x) is statically xs:integer?,
    ($x castable as xs:integer) is statically xs:boolean
)
