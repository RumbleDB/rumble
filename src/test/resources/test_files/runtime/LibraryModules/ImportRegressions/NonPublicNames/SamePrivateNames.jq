(:JIQS: ShouldRun; Output="(x-var, x-fn, local, y-var, y-fn)" :)
(: Only public names must be unique in a namespace, so its modules may each have the same private names. :)
import module namespace m = "urn:import-regression:same-private" at "private-x.jq", "private-y.jq";
(m:x(), m:y())
