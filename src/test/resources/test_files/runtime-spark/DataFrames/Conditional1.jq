(:JIQS: ShouldRun; Output="(foo, foo, foo, foo, foo, foo)" :)
let $df := annotate(
  ({"foo":"foo", "bar":"bar"}, {"foo":"foo", "bar":"bar"}),
  {"foo":"string", "bar":"string"}
)
for $v in (1 to 10)
return
    if (4 > $v)
	then $df.foo
    else $df.foobar
    