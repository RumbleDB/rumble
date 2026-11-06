(:JIQS: ShouldRun; Output="(good, good)" :)
(: A module that imported the failed location resolves its namespace through the location that worked. :)
import module namespace n = "urn:import-regression:fallback" at "fallback-bad.jq", "fallback-good.jq";
import module namespace x = "urn:import-regression:fallback-user" at "fallback-user.jq";
(n:f(), x:g())
