(:JIQS: ShouldRun; Output="(found, true)" :)
(: A module reaches its own private function through a plain QName, and the function reports that same QName. :)
import module namespace m = "urn:import-regression:private-qname" at "private-qname.xq";
m:f()
