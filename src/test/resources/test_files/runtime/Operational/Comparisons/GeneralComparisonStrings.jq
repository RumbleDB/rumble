(:JIQS: ShouldRun; Output="(true, true, true, true, false, false, true, true, true, false, true, true, false)" :)
("ab", "ac") = "ab",
("ab", "ac") = ("ab"),
("ab", "ac", "cd") = ("ac", "ce"),
("ab", "ac") != ("ab", "ac"),
("ab", "ab") != ("ab", "ab"),
("ab") != ("ab", "ab"),
("ac") != ("ab", "ab"),
("ab", "ac") < ("1", "ce"),
("ab", "ac") <= ("1", "ce"),
("xy", "xz") <= ("1", "ce"),
("1", "ce") > ("ab", "ac"),
("1", "ce") >= ("ab", "ac"),
("1.0", "ce") >= ("xy", "xz")
