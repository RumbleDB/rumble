(:JIQS: ShouldRun; Output="true" :)
(: RumbleDB shares JSONiq declared types across the imported modules. XQuery has no such declaration. :)
import module namespace m = "urn:import-regression:types"
    at "fragment-a-functions.jq", "fragment-b-types.jq";
exists(m:f#1)
