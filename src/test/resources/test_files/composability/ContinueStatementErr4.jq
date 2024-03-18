(:JIQS: ShouldNotCompile; ErrorCode="SCCP0001"; ErrorMetadata="LINE:5:COLUMN:14:" :)
switch (2)
case 1 + 1 return "foo";
case 2 + 2 return "bar";
case 3 return continue loop;
default return "none";

(: continue not allowed outside of while or flwor :)