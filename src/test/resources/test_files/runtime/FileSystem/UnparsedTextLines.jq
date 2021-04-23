(:JIQS: ShouldRun; Output="(foo, bar, foobar)" :)
unparsed-text-lines("../../../queries/file.txt"),
unparsed-text-lines(()),
try { unparsed-text-lines(("foo", "bar")) } catch XPTY0004 { () }
