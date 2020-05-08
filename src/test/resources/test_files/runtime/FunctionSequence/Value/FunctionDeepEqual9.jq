(:JIQS: ShouldRun; Output="(false, false, false, false, false, false)" :)
deep-equal((), ("a", 1, [2,3], {"b": 4})),
deep-equal(("a", 1, [2,3], {"b": 4}), ()),
deep-equal([], ("a", 1, [2,3], {"b": 4})),
deep-equal(("a", 1, [2,3], {"b": 4}), []),
deep-equal({}, ("a", 1, [2,3], {"b": 4})),
deep-equal(("a", 1, [2,3], {"b": 4}), {})

(: empty vs non-empty tests :)

