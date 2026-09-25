(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true)" :)
declare namespace eg = "http://example.com";

let $xs-ns := "http://www.w3.org/2001/XMLSchema"
let $qname-constructor := function-lookup(fn:QName($xs-ns, "QName"), 1)
return (
  exists(xs:untypedAtomic#1),
  exists(function-lookup(fn:QName($xs-ns, "untypedAtomic"), 1)),
  (xs:string#1)("foo") eq ("foo" cast as xs:string),
  (function-lookup(fn:QName($xs-ns, "integer"), 1)("12")) eq ("12" cast as xs:integer),
  namespace-uri-from-QName($qname-constructor("eg:local")) eq "http://example.com",
  not(exists(function-lookup(fn:QName($xs-ns, "anyAtomicType"), 1))),
  not(exists(function-lookup(fn:QName($xs-ns, "NOTATION"), 1)))
)
