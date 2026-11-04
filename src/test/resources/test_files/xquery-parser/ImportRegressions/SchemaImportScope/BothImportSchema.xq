(:JIQS: ShouldRun; Output="5" :)
(: Two modules importing one schema see the same type. :)
import schema namespace s = "urn:import-regression:scope-schema" at "scope.xsd";
import module namespace w = "urn:import-regression:scope-with-schema" at "with-schema.xq";
w:same(5 cast as s:T)
