(:JIQS: ShouldRun; Output="(false, false, true)" :)
deep-equal((10, 20, 30), (10, 30, 20)),
deep-equal((10, 20), (10, 20, 30)),
deep-equal((10, 20, "a"), (10, 20, "a"))

(: simple tests :)
