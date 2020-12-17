(:JIQS: ShouldRun; Output="foo\nbar\nfoobar\n" :)
unparsed-text("../../../queries/file.txt"),
unparsed-text(()),
try { unparsed-text(("foo", "bar")) } catch XPTY0004 { () }
