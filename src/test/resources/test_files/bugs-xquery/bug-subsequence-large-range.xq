(:JIQS: ShouldRun; Output="true":)
count(subsequence(1 to 3000000000, -2147483648, 2147483647)) eq 0
and count(subsequence(1 to 3000000000, -2147483649)) eq 3000000000
and count(subsequence(1 to 3000000000, 2147483648)) eq 852516353
and count(subsequence(1 to 3000000000, 2147483647)) eq 852516354
and deep-equal(
    subsequence(1 to 3000000000, 2147483647, 5),
    (2147483647, 2147483648, 2147483649, 2147483650, 2147483651)
)
and count(subsequence(1 to 3000000, -2147483649)) eq 3000000
and deep-equal(subsequence(10 to 20, 2, 3), (11, 12, 13))
and deep-equal(subsequence(10 to 20, -2, 5), (10, 11))
and empty(subsequence(1 to 100, xs:double("NaN")))
and count(subsequence(1 to 100, xs:double("-INF"))) eq 100
and count(subsequence(1 to 1000, -1.0000000000000001e18, 1.0000000000000003e18)) eq 127
and empty(subsequence(1 to 100, xs:double("-INF"), xs:double("INF")))
and empty(subsequence(1 to 100, 3, -1))
and deep-equal(subsequence(1 to 10, 1.5, 2.5), (2, 3, 4))
