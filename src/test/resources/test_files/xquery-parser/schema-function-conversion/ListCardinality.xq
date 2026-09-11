(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
import schema namespace t = "urn:arguments" at "Arguments.xsd";
(: One node atomizes to two values, which cannot satisfy the scalar parameter of abs(). :)
abs(validate strict { <t:numbers>1 2</t:numbers> })
