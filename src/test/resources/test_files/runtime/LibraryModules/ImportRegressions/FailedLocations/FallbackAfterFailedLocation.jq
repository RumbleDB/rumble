(:JIQS: ShouldRun; Output="good" :)
(: A failed location hint leaves a module behind that imports it; the next hint must still be used. :)
import module namespace n = "urn:import-regression:fallback" at "fallback-bad.jq", "fallback-good.jq";
n:f()
