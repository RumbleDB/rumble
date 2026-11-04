(:JIQS: ShouldCrash; ErrorCode="XPST0017" :)
(: Module imports are not transitive: the main module does not see what an imported module imports. :)
import module namespace a = "urn:import-regression:chain-a" at "chain-a.jq";
declare namespace b = "urn:import-regression:chain-b";
b:f()
