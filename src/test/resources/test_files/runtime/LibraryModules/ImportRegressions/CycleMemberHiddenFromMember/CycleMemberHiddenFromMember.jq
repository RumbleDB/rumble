(:JIQS: ShouldCrash; ErrorCode="XPST0017" :)
(: A module in a cycle only sees the namespaces it imports itself, not the rest of the cycle. :)
import module namespace a = "urn:import-regression:triangle-a" at "triangle-a.jq";
a:f()
