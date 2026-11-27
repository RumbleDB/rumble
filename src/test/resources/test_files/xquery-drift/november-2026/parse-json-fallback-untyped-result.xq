(:JIQS: ShouldRun; Output="true":)
(: The fallback is coerced to a function returning xs:string, which converts its untyped result.
   https://www.w3.org/TR/xpath-functions-31/#func-parse-json
   https://www.w3.org/TR/xquery-31/#id-function-coercion :)
let $options := map {
    "fallback": function($escape as xs:string) as xs:untypedAtomic {
        xs:untypedAtomic("fallback")
    }
}
return parse-json('"\u0000"', $options) eq "fallback"
