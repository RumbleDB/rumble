(:JIQS: ShouldRun; Output="3" :)
(: Each nested import must honor its own explicit location for urn:import-regression:b. :)
import module namespace a = "urn:import-regression:a" at "nested-a.jq";
a:f()
