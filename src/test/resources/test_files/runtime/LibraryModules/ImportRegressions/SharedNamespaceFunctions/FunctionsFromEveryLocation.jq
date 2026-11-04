(:JIQS: ShouldRun; Output="(a-fn, b-fn)" :)
(: Importing a namespace provides the public functions of the modules at all of its location hints. :)
import module namespace m = "urn:import-regression:shared-functions" at "location-a.jq", "location-b.jq";
(m:a(), m:b())
