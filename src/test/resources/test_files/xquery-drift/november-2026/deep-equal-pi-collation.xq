(:JIQS: ShouldRun; Output="true":)
(: F&O 3.1 §14.2.3 applies the selected collation when comparing PI string values.
   https://www.w3.org/TR/xpath-functions-31/#func-deep-equal
   Observed on master c6cbdb7fa: false instead of true. :)
deep-equal(
    processing-instruction p {"A"},
    processing-instruction p {"a"},
    "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive"
)
