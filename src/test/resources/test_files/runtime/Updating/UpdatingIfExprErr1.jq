(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := {"a" : 1, "b" : 2}
modify if(delete $je.c)
       then
           delete $je.c
       else
           insert "c" : 3 into $je
return $je

(: expr in if clause is not simple :)