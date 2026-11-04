(:JIQS: ShouldCrash; ErrorCode="XPST0017" :)
(: Importing a module in a cycle does not import the other modules of that cycle. :)
import module namespace a = "urn:import-regression:cycle-a" at "cycle-a.jq";
declare namespace b = "urn:import-regression:cycle-b";
b:g(0)
