(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := {"a" : 1, "b" : 2}
modify if(delete json $je.c)
       then
           delete json $je.c
       else
           insert json "c" : 3 into $je
return $je

(: expr in if clause is not simple :)