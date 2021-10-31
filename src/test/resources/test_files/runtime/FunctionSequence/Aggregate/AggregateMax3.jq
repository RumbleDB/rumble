(:JIQS: ShouldRun; Output="(true, true, NaN, true, true,)" :)
max((xs:float("23.987"), xs:nonNegativeInteger(23), xs:float(2))) instance of xs:float,
max((xs:float(2000), 1, 1, 2, xs:double(40))) instance of xs:double,
max((xs:float("NaN"))),
max((xs:int(10), xs:positiveInteger(43))) instance of xs:positiveInteger,
max(("a", "bac", "sun")) eq "sun",
max(())