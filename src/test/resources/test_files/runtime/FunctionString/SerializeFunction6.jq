(:JIQS: ShouldRun; Output="(100, 100000, 1.0E6, 1.0E7, 0.1, 0.0001, 0.000001, 1.0E-7)" :)
serialize(double(100)),
serialize(double(100000)),
serialize(double(1000000)),
serialize(double(10000000)),
double(0.100),
double(0.0001),
double(0.000001),
double(0.0000001)
(: test double serialization :)
