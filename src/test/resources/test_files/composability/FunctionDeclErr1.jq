(:JIQS: ShouldNotCompile; ErrorCode="XQAN0001"; ErrorMetadata="LINE:2:COLUMN:0:" :)
declare %sequential %nonsequential function foo() { 1 };

(: it is a static error to have both sequential and nonsequential annotations :)