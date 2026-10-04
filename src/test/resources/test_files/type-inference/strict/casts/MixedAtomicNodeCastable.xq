(:JIQS: ShouldRun; Output="(true, true, false, false)" :)
for $x in ('3', <a>4</a>, <b>invalid</b>, 'invalid')
return ($x castable as xs:integer) is statically xs:boolean
