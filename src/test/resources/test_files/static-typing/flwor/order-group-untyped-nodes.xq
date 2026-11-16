(:JIQS: ShouldRun; Output="(1, 2, 1, 2, 2, 1, 2)" :)
(: The typed value of a parsed element is only known at runtime, unlike that of a constructed one. :)
declare function local:node($xml as xs:string) as element() { exactly-one(parse-xml($xml)/*) };
(: The typed value of these nodes is only known at runtime, so the atomized keys are checked then. :)
(for $x in (local:node("<a>2</a>"), local:node("<a>1</a>")) order by $x return data($x)),
(for $x in (local:node("<a v='2'/>"), local:node("<a v='1'/>")) order by $x/@v return data($x/@v)),
(for $x in (local:node("<a>2</a>"), local:node("<a>1</a>")) group by $k := $x return $k is statically xs:anyAtomicType?),
(for $x in (local:node("<a>2</a>"), local:node("<a>2</a>")) group by $x return $x is statically xs:anyAtomicType?)
