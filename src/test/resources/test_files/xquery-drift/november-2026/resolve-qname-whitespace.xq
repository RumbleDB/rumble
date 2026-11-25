(:JIQS: ShouldRun; Output="true":)
(: F&O 3.1 §10.1.1 accepts the lexical form of xs:QName, whose whitespace facet is collapse.
   https://www.w3.org/TR/xpath-functions-31/#func-resolve-QName
   Observed on master c6cbdb7fa: FOCA0002 instead of true. :)
local-name-from-QName(resolve-QName(" a ", <r/>)) eq "a"
