(:JIQS: ShouldRun; Output="true":)
(: XQuery 3.1 §3.9.3.5 removes leading XML whitespace, not U+2003 EM SPACE.
   https://www.w3.org/TR/xquery-31/#id-computedPIs
   Observed on master c6cbdb7fa: false; U+2003 is incorrectly removed. :)
let $content := codepoints-to-string((8195, 120))
return string(processing-instruction p {$content}) eq $content
