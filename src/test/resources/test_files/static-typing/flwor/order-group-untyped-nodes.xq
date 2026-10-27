(:JIQS: ShouldRun; Output="(1, 2, 1, 2, 2, 1, 2)" :)
(: The typed value of these nodes is only known at runtime, so the atomized keys are checked then. :)
(for $x in (<a>2</a>, <a>1</a>) order by $x return data($x)),
(for $x in (<a v="2"/>, <a v="1"/>) order by $x/@v return data($x/@v)),
(for $x in (<a>2</a>, <a>1</a>) group by $k := $x return $k is statically xs:anyAtomicType?),
(for $x in (<a>2</a>, <a>2</a>) group by $x return $x is statically xs:anyAtomicType?)
