(:JIQS: ShouldNotCompile; ErrorCode="SCCP0001"; ErrorMetadata="LINE:4:COLUMN:4:" :)
if (true) then {
    $x := 1 + 1;
    break loop;
} else {();}

(: break not allowed outside of while or flwor :)