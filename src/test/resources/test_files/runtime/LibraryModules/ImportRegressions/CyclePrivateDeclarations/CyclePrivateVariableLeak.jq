(:JIQS: ShouldNotCompile; ErrorCode="XPST0008"; ErrorMetadata="LINE:4:COLUMN:25:" :)
(: A module cannot read another module's private variable, whichever sorts first in their group. :)
import module namespace a = "urn:import-regression:leak-variable-a" at "leak-variable-a.jq";
a:f()
