(:JIQS: ShouldRun; Output="(1'000'000, 0'015, 1'000'000, 15, 1'234'567, 123)" :)
format-integer(1000000, "0'000"),
format-integer(15, "0'000"),
format-integer(1000000, "#'##0"),
format-integer(15, "#'##0"),
format-integer(1234567, "#'##0"),
format-integer(123, "#'##0")
