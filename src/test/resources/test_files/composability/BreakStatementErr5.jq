(:JIQS: ShouldNotCompile; ErrorCode="SCCP0004"; ErrorMetadata="LINE:3:COLUMN:2:" :)
try {
  break loop;
} catch * {
  "Break not allowed!";
}

(: break not allowed outside of while or flwor :)