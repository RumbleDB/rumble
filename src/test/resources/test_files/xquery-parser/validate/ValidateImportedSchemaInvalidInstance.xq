(:JIQS: ShouldCrash; ErrorCode="XQDY0027" :)
import schema namespace t = "urn:validate-test" at "ValidateImportedSchema.xsd";

validate strict { <t:root><t:amount>-1</t:amount></t:root> }    (: Because the min value of t:amount is 0 inclusive, the value -1 is invalid. :)
