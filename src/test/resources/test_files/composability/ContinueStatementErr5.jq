(:JIQS: ShouldNotCompile; ErrorCode="SCCP0004"; ErrorMetadata="LINE:3:COLUMN:2:" :)
try {
  continue loop;
} catch * {
  "Continue not allowed!";
}

(: continue not allowed outside of while or flwor :)