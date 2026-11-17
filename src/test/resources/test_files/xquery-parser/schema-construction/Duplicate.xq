(:JIQS: ShouldNotCompile; ErrorCode="XQST0067" :)
(: A module may declare its construction mode only once. The second declaration
   is a static error (XQST0067), rather than an override of the first. :)
declare construction preserve;
declare construction strip;
1
