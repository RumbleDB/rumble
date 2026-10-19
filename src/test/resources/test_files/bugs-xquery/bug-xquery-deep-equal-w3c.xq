(:JIQS: ShouldRun; Output="(true, true, false, false, false, false, true, false, err, err)" :)
deep-equal(xs:double("NaN"), xs:double("NaN")),
deep-equal(xs:float("NaN"), xs:double("NaN")),
deep-equal(1, current-dateTime()),
deep-equal(1, "1"),
deep-equal(1, [1]),
deep-equal([1], map {"1": 1}),
deep-equal((), ()),
deep-equal((), 1),
try { deep-equal(fn:abs#1, fn:abs#1) } catch err:FOTY0015 { "err" },
try { deep-equal(fn:abs#1, 1) } catch err:FOTY0015 { "err" }
