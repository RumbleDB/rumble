(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
import schema namespace n = "urn:notation" at "Notation.xsd";
(: NOTATION supports equality, but has no ordering relation. :)
n:Image("n:jpeg") lt n:Image("n:png")
