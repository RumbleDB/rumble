(:JIQS: ShouldNotCompile; ErrorCode="SCCP0006"; ErrorMetadata="LINE:3:COLUMN:0:" :)
declare %sequential function bar() { 1 };
declare %nonsequential function foo() {
    bar()
 };

(: declared nonsequential with sequential body is not valid :)