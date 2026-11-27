(:JIQS: ShouldRun; Output="true":)
(: The search visits both items in the array's single member in sequence order.
   https://www.w3.org/TR/xpath-functions-31/#func-map-find :)
let $input := [(map {"k": 1}, map {"k": 2})]
return deep-equal(map:find($input, "k"), [1, 2])
