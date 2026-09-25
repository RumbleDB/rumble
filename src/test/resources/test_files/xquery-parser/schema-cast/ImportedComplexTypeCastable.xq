(:JIQS: ShouldNotCompile; ErrorCode="XQST0052" :)
import schema namespace t = "urn:cast-test" at "ImportedSimpleTypes.xsd";

"value" castable as t:Record (: castable as rejects an imported complex type :)
