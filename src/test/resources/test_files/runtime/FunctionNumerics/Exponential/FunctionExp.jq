(:JIQS: ShouldRun; Output="(true, true, true)" :)
(: Allow small floating-point rounding differences between JVM implementations. :)
exp(0) eq 1,
abs(exp(1) - (2.718281828459045)) lt 1e-15,
empty(exp(()))
