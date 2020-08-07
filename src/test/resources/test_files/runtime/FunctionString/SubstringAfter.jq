(:JIQS: ShouldRun; Output="(lo, &Again, , hello, hello, , , , , , , )" :)
substring-after("hello", "el"),
substring-after("helloAgain&Again", "Again"),
substring-after("hello", "hello"),
substring-after("hello", ""),
substring-after("hello", ()),
substring-after("", ""),
substring-after("", ()),
substring-after((), ""),
substring-after((), ()),
substring-after((), "hello"),
substring-after("", "hello"),
substring-after("hello", "cat")

(: general tests :)
