(:JIQS: ShouldNotCompile; ErrorCode="SCCP0001"; ErrorMetadata="LINE:5:COLUMN:20:" :)
typeswitch([1,2])
case string return "string";
case $a as boolean+ | array* return $a;
case integer return break loop;
default return "default";

(: break not allowed outside of while or flwor :)