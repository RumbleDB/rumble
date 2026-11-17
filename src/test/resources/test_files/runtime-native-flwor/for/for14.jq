(:JIQS: ShouldRun; Output="" :)
for $x in annotate({"foo":2},{"foo":"integer"})
for $y in $x.foobar[]
return $x