(:JIQS: ShouldCrash; ErrorCode="XPST0017"; ErrorMetadata="LINE:4:COLUMN:25:" :)
(: A module cannot call another module's private function, whichever sorts first in their group. :)
import module namespace a = "urn:import-regression:leak-function-a" at "leak-function-a.jq";
a:f()
