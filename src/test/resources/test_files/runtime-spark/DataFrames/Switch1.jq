(:JIQS: ShouldRun; Output="(foo, foo, foo, foo, foo, foo, bar, bar, bar, bar, bar, bar, bar, bar, bar, bar)" :)
let $df := annotate(
  ({"foo":"foo", "bar":"bar"}, {"foo":"foo", "bar":"bar"}),
  {"foo":"string", "bar":"string"}
)
for $v in (1 to 10)
return
    switch (true)
    case (4 > $v) return $df.foo
    case (6 > $v) return $df.foobar
    default return $df.bar
        