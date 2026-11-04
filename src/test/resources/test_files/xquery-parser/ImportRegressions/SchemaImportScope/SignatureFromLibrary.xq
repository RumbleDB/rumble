(:JIQS: ShouldRun; Output="7" :)
(: A function's signature keeps using its own module's schema type, wherever it is called from. :)
import module namespace w = "urn:import-regression:scope-with-schema" at "with-schema.xq";
w:same(w:make())
