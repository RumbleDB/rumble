(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := {"a" : 1, "b" : 2}
modify if(delete json $je.c)
       then
           delete json $je.c
       else
           3
return $je

(: one branch expr is not simple :)