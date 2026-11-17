(:JIQS: ShouldNotCompile; ErrorCode="XQST0099" :)
(: A module cannot contain more than one context item declaration. :)
declare context item external;
declare context item := 5;
1
