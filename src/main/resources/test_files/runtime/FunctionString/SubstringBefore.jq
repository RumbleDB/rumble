(:JIQS: ShouldRun; Output="(hell, hello, , , , , , , , , , )" :)
substring-before("hello", "o"),
substring-before("helloAgain&Again", "Again"),
substring-before("hello", "hello"),
substring-before("hello", ""),
substring-before("hello", ()),
substring-before("", ""),
substring-before("", ()),
substring-before((), ""),
substring-before((), ()),
substring-before((), "hello"),
substring-before("", "hello"),
substring-before("hello", "cat")

(: general tests :)
