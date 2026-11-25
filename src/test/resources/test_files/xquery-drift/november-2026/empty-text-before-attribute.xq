(:JIQS: ShouldRun; Output="true":)
(: XQuery 3.1 §3.9.3.1 deletes empty text nodes before checking attribute ordering.
   https://www.w3.org/TR/xquery-31/#id-computedElements
   Observed on master c6cbdb7fa: XQTY0024 instead of true. :)
count(element a {text {""}, attribute x {"y"}}/@x) eq 1
