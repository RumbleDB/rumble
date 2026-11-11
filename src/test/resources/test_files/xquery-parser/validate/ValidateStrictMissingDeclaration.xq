(:JIQS: ShouldCrash; ErrorCode="XQDY0084" :)
import schema namespace t = "urn:validate-test" at "ValidateImportedSchema.xsd";

validate strict { <t:unknown/> }
