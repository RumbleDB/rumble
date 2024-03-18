(:JIQS: ShouldNotCompile; ErrorCode="SCCP0001"; ErrorMetadata="LINE:2:COLUMN:0:" :)
declare %an:nonsequential function foo() { $var := 3 + 3; };

(: declared nonsequential with sequential body is not valid :)