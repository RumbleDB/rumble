(:JIQS: ShouldRun; Output="true":)
(: Observed on master f1775366d: XPTY0004 instead of a callable partially applied function. :)
(: https://www.w3.org/TR/xquery-31/#id-eval-function-call :)
(: Partial application of a map must produce a callable function. :)
let $f := map {"a": 1}(?)
return $f("a") eq 1
