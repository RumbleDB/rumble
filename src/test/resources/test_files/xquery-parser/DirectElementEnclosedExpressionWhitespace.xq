(:JIQS: ShouldRun; Output="(true, true, true, true, true, true)" :)
declare boundary-space preserve;
(
  string(<e>{1} {2}</e>) eq "1 2",
  deep-equal(string-to-codepoints(string(<e>{1}
{2}</e>)), (49, 10, 50)),
  deep-equal(string-to-codepoints(string(<e>{1}	{2}</e>)), (49, 9, 50)),
  string(<e> {1} </e>) eq " 1 ",
  string(<e> </e>) eq " ",
  string(<e> <![CDATA[]]> </e>) eq "  "
)
