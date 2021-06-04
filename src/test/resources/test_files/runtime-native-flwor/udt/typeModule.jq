(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace typeModule = "typeModule.jq";

declare type typeModule:fooIntegers as { "foo" : "integer" };
declare type typeModule:fooArrayOfIntegers as { "foo" : [ "integer" ] };
declare type typeModule:requiredFooArrayOfIntegers as { "!foo" : [ "integer" ] };
