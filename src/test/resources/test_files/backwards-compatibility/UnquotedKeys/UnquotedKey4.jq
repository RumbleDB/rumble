jsoniq version "1.0";
(:JIQS: ShouldRun; Output="{ "ukey" : "val", "ukey2" : "otherval" }" :)
{ukey:"val",
    ukey2: "otherval"}

(: key were allowed to be unquoted in jsoniq1.0 :)