(:JIQS: ShouldNotCompile; ErrorCode="SCCP0006"; ErrorMetadata="LINE:4:COLUMN:0:" :)
declare %an:sequential function barseq() { 0 };
declare function bar() { barseq() };
declare %an:nonsequential function foo() {bar()};

(: declared nonsequential with sequential body is not valid :)