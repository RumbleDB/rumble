(:JIQS: ShouldRun; Output="(true, true, true, true)" :)
deep-equal((), ()),
deep-equal([], []),
deep-equal({}, {}),
deep-equal(parallelize(()), parallelize(()))

(: empty tests :)

