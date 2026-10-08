(:JIQS: ShouldRun; Output="(1, 2)" :)
for $x in (<a>1</a>, text {"2"})
return ($x cast as xs:integer) is statically xs:integer
