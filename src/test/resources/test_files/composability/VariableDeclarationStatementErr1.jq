(:JIQS: ShouldNotCompile; ErrorCode="SCCP0002"; ErrorMetadata="LINE:3:COLUMN:0:" :)
variable $je := { "a" : 1, "b" : 2, "c" : 3 };
variable $var := replace json value of $je.a with 4;

(: variable declaration not allowed with updating expressions :)