(:JIQS: ShouldNotCompile; ErrorCode="SCCP0001"; ErrorMetadata="LINE:2:COLUMN:0:" :)
declare %an:sequential function bar() { 1 };
declare %an:nonsequential function foo() {bar()};

(: declared nonsequential with sequential body is not valid :)