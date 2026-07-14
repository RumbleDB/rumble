(:JIQS: ShouldRun; Output="(true, true, true)" :)
("&amp;&quot;" eq "&amp;&quot;",
string-length("&amp;&quot;") eq 11,
data(<e x="&amp;&quot;"/>/@x) eq "&\"",
data(<e x="\n"/>/@x) eq "\\n")
