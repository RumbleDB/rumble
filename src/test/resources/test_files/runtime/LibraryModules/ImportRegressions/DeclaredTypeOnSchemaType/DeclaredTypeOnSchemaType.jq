(:JIQS: ShouldRun; Output="5" :)
(: A declared type resolves in its own module, whose schema import the importer does not see. :)
import module namespace l = "urn:l" at "declared-lib.jq";
l:f()
