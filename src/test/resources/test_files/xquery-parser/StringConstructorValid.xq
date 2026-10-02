(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true, true, true)" :)
(
  (``[]``) eq "",
  (``[Hello World]``) eq "Hello World",
  (``[Hello `{"World"}`!]``) eq "Hello World!",
  (``[Count: `{1 to 5}`]``) eq "Count: 1 2 3 4 5",
  (``[`{1}` + `{2}` = `{3}`]``) eq "1 + 2 = 3",
  (``[a`{}`b]``) eq "ab",
  (``[&lt;&gt;&amp;]``) eq "&amp;lt;&amp;gt;&amp;amp;",
  string(<a>Today is `{xs:date('2012-05-05')}`</a>) eq "Today is `2012-05-05`",
  (``[`{ 1, ``[literal text]``, 2 }`]``) eq "1 literal text 2",
  (``[` {$n}`]``) eq "` {$n}`"
)
