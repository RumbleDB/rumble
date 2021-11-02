(:JIQS: ShouldRun; Output="(true, true, NaN, true, true, true)" :)
min((xs:float("23.987"), xs:nonNegativeInteger(23), xs:float(2))) instance of xs:float,
min((xs:float(2000), 1, 1, 2, xs:double(40))) instance of xs:double,
min((xs:float("NaN"))),
min((xs:int(1), xs:positiveInteger(43))) instance of xs:int,
min(("a", "bac", "sun")) eq "a",
min((xs:int(23), xs:decimal(10))) instance of xs:decimal,
min(())