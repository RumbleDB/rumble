(:JIQS: ShouldRun; Output="(true, true, false)" :)
for $x in ('text', <a/>, '')
return if ($x) then true() else false()
