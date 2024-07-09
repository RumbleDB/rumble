(:JIQS: ShouldNotCompile; ErrorCode="SCCP0006"; ErrorMetadata="LINE:3:COLUMN:0:" :)
declare %an:sequential function bar() { 1 };
declare %an:nonsequential function foo() {
    bar()
 };

(: declared nonsequential with sequential body is not valid :)