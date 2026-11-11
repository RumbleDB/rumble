(:JIQS: ShouldRun; Output="(1-a-true, 12)" :)
jsoniq version "3.1";
(: Since version 3.1, string-join accepts atomic items; version 1.0 requires strings, as StringJoinError1.jq shows. :)
string-join((1, "a", true), "-"),
string-join#1((1, 2))
