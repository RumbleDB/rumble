(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
import schema namespace n = "urn:notation" at "Notation.xsd";
(: NOTATION and QName are distinct primitive types even when their expanded names match. :)
n:Image("n:jpeg") eq QName("urn:notation", "n:jpeg")
