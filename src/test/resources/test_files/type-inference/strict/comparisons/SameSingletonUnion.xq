(:JIQS: ShouldRun; Output="(true, true)" :)
(: Both references denote the same singleton, so each type is compared only with itself. :)
for $x in (1, "a")
return ($x eq $x) is statically xs:boolean
