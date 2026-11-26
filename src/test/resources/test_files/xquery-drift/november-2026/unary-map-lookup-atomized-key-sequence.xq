(:JIQS: ShouldRun; Output="true":)
(: Unary lookup calls the context map once for each atomized key, in key order.
   https://www.w3.org/TR/xquery-31/#id-unary-lookup :)
let $map := map {1: "one", 2: "two"}
return deep-equal($map ! ?([2, 1]), ("two", "one"))
