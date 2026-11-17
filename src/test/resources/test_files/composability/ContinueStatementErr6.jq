(:JIQS: ShouldNotCompile; ErrorCode="SCCP0004"; ErrorMetadata="LINE:5:COLUMN:20:" :)
typeswitch([1,2])
case string return "string";
case $a as boolean+ | array* return $a;
case integer return continue loop;
default return "default";

(: continue not allowed outside of while or flwor :)