(:JIQS: ShouldNotCompile; ErrorCode="SCCP0004"; ErrorMetadata="LINE:4:COLUMN:4:" :)
if (true) then {
    variable $x := 1 + 1;
    break loop;
} else {();}

(: break not allowed outside of while or flwor :)