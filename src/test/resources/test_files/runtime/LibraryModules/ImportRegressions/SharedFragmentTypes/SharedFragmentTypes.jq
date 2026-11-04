(:JIQS: ShouldRun; Output="true" :)
(: The function signature must resolve a type declared in another fragment. :)
import module namespace m = "urn:import-regression:types"
    at "fragment-a-functions.jq", "fragment-b-types.jq";
exists(m:f#1)
