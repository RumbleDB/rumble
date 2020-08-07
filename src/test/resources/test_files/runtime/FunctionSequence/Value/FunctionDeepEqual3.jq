(:JIQS: ShouldRun; Output="(true, true, true)" :)
deep-equal([1], [1]),
deep-equal([2.0, "a"], [2e+0, "a"]),
deep-equal(["a", [1, 2]], ["a", [1.0, 2.0]])

(: array tests - matching :)

