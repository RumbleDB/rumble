(:JIQS: ShouldCrash; ErrorCode="FORG0001" :)
import schema namespace n = "urn:notation" at "Notation.xsd";
(: Casting an existing NOTATION value must still enforce the target enumeration. :)
n:Jpeg(n:Image("n:png"))
