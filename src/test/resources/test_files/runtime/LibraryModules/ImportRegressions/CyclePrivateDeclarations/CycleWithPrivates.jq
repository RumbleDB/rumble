(:JIQS: ShouldRun; Output="(a-secret, b-hidden)" :)
(: Mutually importing modules use their own private declarations, including mutually recursive ones. :)
import module namespace a = "urn:import-regression:cycle-a" at "cycle-a.jq";
(a:f(2), a:f(1))
