(:JIQS: ShouldRun; Output="5" :)
(: A partial constructor call in a library module resolves against that module's schema imports. :)
import module namespace m = "urn:import-regression:partial" at "partial-lib.xq";
m:f()
