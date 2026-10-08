(:JIQS: ShouldRun; Output="2" :)
(: Node atomization may be empty, so the optional target stays optional in inference. :)
(text {"2"} cast as xs:integer?) is statically xs:integer?
