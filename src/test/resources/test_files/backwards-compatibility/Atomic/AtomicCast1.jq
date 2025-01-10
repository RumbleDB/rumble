(:JIQS: ShouldRun; Output="(3, 3, 12, 12, 33, true, 0)" :)
(3 cast as decimal) is statically decimal, (3 cast as string) is statically string, ((12 treat as atomic) cast as string) is statically string, (12 cast as double) is statically double, (33.23 cast as integer) is statically integer, (3 cast as boolean) is statically boolean, (false cast as integer) is statically integer

(: anyAtomicType was atomic in jsoniq1.0 :)