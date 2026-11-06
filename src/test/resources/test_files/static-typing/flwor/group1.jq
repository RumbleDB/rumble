(:JIQS: ShouldRun :)
(for $a in (1,2,3)
group by $b := $a mod 2
return $b) is statically integer+