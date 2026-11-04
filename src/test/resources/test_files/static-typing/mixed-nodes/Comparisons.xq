(:JIQS: ShouldRun; Output="(true, true, true, false)" :)
(: A value comparison compares untyped values as strings, a general comparison as the other operand's type. :)
(for $x in ('3', <a>3</a>) return ($x eq '3') is statically xs:boolean?),
(for $x in (3, <a>4</a>) return ($x = 3) is statically xs:boolean)
