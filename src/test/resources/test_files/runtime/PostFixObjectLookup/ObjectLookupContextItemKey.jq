(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; :)
(: A null context item key is rejected like any other null key, not looked up as "null". :)
(null)[{"null": "x"}.$$ eq "x"]
