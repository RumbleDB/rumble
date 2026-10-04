(:JIQS: ShouldRun; Output="(3, 5)" :)
for $x in (xs:decimal('3.4'), <a>4.5</a>)
return round($x) is statically xs:numeric?
