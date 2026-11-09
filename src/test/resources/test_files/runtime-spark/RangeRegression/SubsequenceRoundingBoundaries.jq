(:JIQS: ShouldRun; Output="true" :)
(: Half ties and the adjacent representable doubles. :)
subsequence(1 to 10, 1.5, 1) eq 2
and subsequence(1 to 10, 1.4999999999999998e0, 1) eq 1
and subsequence(1 to 10, -0.5, 2) eq 1
and empty(subsequence(1 to 10, -0.5000000000000001e0, 2))
and empty(subsequence(1 to 10, 1, 0.49999999999999994e0))
(: Position comparisons above 2^53 must preserve integer-to-double promotion. :)
and deep-equal(subsequence(1 to 9007199254741000, 9007199254740992e0, 2),
  (9007199254740992, 9007199254740993))
and empty(subsequence(1 to 9007199254741000, xs:double("INF")))
and count(subsequence(1 to 9007199254741000, xs:double("-INF"))) eq 9007199254741000
