(:JIQS: ShouldRun; Output="(true, true, true)" :)
(: Allow small floating-point rounding differences between JVM implementations. :)
cos(0) eq 1,
abs(cos(3.1428) - (-0.9999992711574114)) lt 1e-15,
empty(cos(()))
