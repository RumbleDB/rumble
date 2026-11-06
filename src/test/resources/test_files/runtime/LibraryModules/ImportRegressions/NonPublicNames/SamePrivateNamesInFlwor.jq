(:JIQS: ShouldRun; Output="(11, 12, 21, 22, 2, 3)" :)
(: Same-named private globals stay apart in the FLWOR expressions using them, and a local can still shadow one. :)
import module namespace f = "urn:import-regression:non-public-flwor" at "flwor-a.jq", "flwor-b.jq";
f:a(),
f:b(),
f:shadowed()
