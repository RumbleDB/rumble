(:JIQS: ShouldCrash; ErrorCode="XPST0017" :)
(: Sharing a target namespace gives a module no access to another's functions: it must import that namespace. :)
import module namespace m = "urn:import-regression:hidden-functions" at "hidden-caller.jq", "hidden-callee.jq";
m:caller()
