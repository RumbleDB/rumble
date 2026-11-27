(:JIQS: ShouldRun; Output="true":)
(: Postfix lookup calls the map once for each atomized key, in key order.
   https://www.w3.org/TR/xquery-31/#id-postfix-lookup :)
let $map := map {1: "one", 2: "two"}
return deep-equal($map?([2, 1]), ("two", "one"))
