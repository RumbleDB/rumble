jsoniq version "3.1";
(:JIQS: ShouldNotParse; ErrorCode="XPST0003" :)
{ukey:"val",
    ukey2: "otherval"}

(: unquoted keys are error in jsoniq 3.1 :)