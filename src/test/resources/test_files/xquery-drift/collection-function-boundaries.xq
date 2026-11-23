(:JIQS: ShouldRun; Output="true":)
let $map := map {"a": (1, 2)}
let $array := [(1, 2)]
let $mf := $map(?)
let $af := $array(?)
return
  empty(function-name($map)) and empty(function-name($array))
  and function-arity($map) eq 1 and function-arity($array) eq 1
  and empty(function-name($mf)) and empty(function-name($af))
  and function-arity($mf) eq 1 and function-arity($af) eq 1
  and not($mf instance of map(*)) and not($af instance of array(*))
  and deep-equal($mf("a"), (1, 2))
  and deep-equal($af(xs:untypedAtomic("1")), (1, 2))
  and deep-equal(($af(?))(1), (1, 2))
  and function-arity(concat#2) eq 2
  and local-name-from-QName(function-name(concat#2)) eq "concat"
