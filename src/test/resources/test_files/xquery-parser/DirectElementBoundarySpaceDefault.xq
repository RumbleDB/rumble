(:JIQS: ShouldRun; Output="(true, true, true, true, true, true)" :)
(
  string(<e>{1} {2}</e>) eq "12",
  string(<e><a/> <b/></e>) eq "",
  string(<e> </e>) eq "",
  string(<e> <![CDATA[]]> </e>) eq "",
  string(<e><![CDATA[ ]]></e>) eq " ",
  string(<e>&#x20;</e>) eq " "
)
