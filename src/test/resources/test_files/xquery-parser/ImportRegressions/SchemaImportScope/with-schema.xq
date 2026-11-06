(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace w = "urn:import-regression:scope-with-schema";
import schema namespace s = "urn:import-regression:scope-schema" at "scope.xsd";
declare function w:same($value as s:T) as s:T { $value };
declare function w:make() as s:T { 7 cast as s:T };
