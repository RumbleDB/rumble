(:JIQS: ShouldRun; Output="(First, First, Twenty-One, 21st)" :)
format-integer(1, "Ww;o(%spellout-ordinal)", "en"),
format-integer(1, "Ww;o(-zzz)", "en"),
format-integer(21, "Ww;c(-zzz)", "en"),
format-integer(21, "1;o(-zzz)", "en")
