(:JIQS: ShouldRun; Output="(1, 2, 3, 4, 5)" :)
declare namespace ns1 = "http://example.org/ns1";
declare namespace ns2 = "http://example.org/ns2";
(
  (# ns1:test-pragma some content #) { 1 },
  (#Q{http://example.org/ns1}test #) { 2 },
  (#Q{}unprefixed #) { 3 },
  (# my-pragma #) { 4 },
  (# ns1:p1 #) (# ns2:p2 extra info #) { 5 }
)
