(:JIQS: ShouldCrash; ErrorCode="XPDY0002":)
(: The body of an inline function has an absent focus even when the main query has a context item.
   https://www.w3.org/TR/xquery-31/#id-eval-function-call :)
declare context item := <a/>;
(function() { . })()
