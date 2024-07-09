(:JIQS: ShouldNotCompile; ErrorCode="SCCP0001"; ErrorMetadata="LINE:3:COLUMN:0:" :)
declare %an:sequential function foo() { 1 };
declare variable $var := foo();

(: variable declaration not allowed with non-updating and non-sequential expressions :)