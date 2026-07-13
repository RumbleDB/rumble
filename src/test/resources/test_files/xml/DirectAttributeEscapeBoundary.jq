(:JIQS: ShouldRun; Output="(true, true, true)" :)
("&amp;&quot;" eq "&amp;&quot;",
 data(<e x="&amp;&quot;"/>/@x) eq "&\"",
 data(<e x="\n"/>/@x) eq "\\n")
