(:JIQS: ShouldRun; Output="true":)
(: XQuery 3.1 §3.4.2 permits union of nodes, including standalone direct PIs.
   https://www.w3.org/TR/xquery-31/#combining_seq
   Observed on master c6cbdb7fa: XPTY0004 because the PIs have no document positions. :)
count((<?p x?>, <?p x?>) union ()) eq 2
