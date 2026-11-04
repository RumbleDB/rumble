(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace l = "urn:l";
import schema namespace s = "urn:import-regression:scope-schema" at "scope.xsd";
declare type l:D as jsound verbose { "kind": "atomic", "baseType": "s:T", "maxInclusive": 100 };
declare function l:f() { validate type l:D { 5 } };
