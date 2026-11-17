(:JIQS: ShouldRun; Output="{ "foo" : 2 }" :)
for $x in annotate({"foo":2},{"foo":"integer"})
group by $y := $x.foobar
return $x