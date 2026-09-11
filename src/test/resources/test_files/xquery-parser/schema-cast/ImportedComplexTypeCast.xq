(:JIQS: ShouldNotCompile; ErrorCode="XQST0052" :)
import schema namespace t = "urn:cast-test" at "ImportedSimpleTypes.xsd";

"value" cast as t:Record (: cast as rejects an imported complex type :)
