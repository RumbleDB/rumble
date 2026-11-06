(:JIQS: ShouldNotCompile; ErrorCode="XPST0008"; ErrorMetadata="LINE:5:COLUMN:7:" :)
declare namespace test = "http://www.example.com/1";
declare namespace test2 = "http://www.example.com/2";
let $test:var := 1
return $test2:var
