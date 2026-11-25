(:JIQS: ShouldRun; Output="true":)
(: XQuery 3.1 §2.4.1 and §3.7.3 require a consistent order for distinct nodes.
   https://www.w3.org/TR/xquery-31/#id-node-comparisons
   Observed on master c6cbdb7fa: XPTY0004 because the comments have no document positions.
   Either relative order is allowed; the test does not prescribe one. :)
let $a := <!--a-->, $b := <!--b-->
return ($a << $b) or ($b << $a)
