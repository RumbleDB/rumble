(:JIQS: ShouldRun; Output="ok" :)
xquery version "3.1";
(: Both library modules import the schema independently; the caller does not need it. :)
import module namespace m = "urn:schema-isolation" at "b-schema.xqm", "d-imports-schema.xqm";
m:use(m:make())
