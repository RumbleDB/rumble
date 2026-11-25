(:JIQS: ShouldRun; Output="true":)
(: XQuery 3.1 §3.9.3.7 tests castability to xs:NCName, whose whitespace facet is collapse.
   https://www.w3.org/TR/xquery-31/#id-computedNamespaces
   Observed on master c6cbdb7fa: XQDY0074 instead of true.
   Only existence is asserted, without prescribing the stored prefix spelling. :)
exists(namespace {" p "} {"urn:test"})
