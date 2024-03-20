(:JIQS: ShouldNotCompile; ErrorCode="SCCP0001"; ErrorMetadata="LINE:4:COLUMN:4:" :)
if (true) then {
    variable $x := 1 + 1;
    continue loop;
} else {();}

(: continue not allowed outside of while or flwor :)