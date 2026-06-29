(:JIQS: ShouldRun; Output="(true, true, false, false, false, false, false, false, true, true, false, true)" :)
matches("x[Y-z]", "X[y-Z]", "qi"),
matches("AbC", "abc", "i"),
matches("AbC", "abc"),
matches("O", "[A-Z-[OI]]", "i"),
matches("I", "[A-Z-[OI]]", "i"),
matches("q", "[^Q]", "i"),
matches("Q", "[^q]", "i"),
matches("é", "[^xÉ]", "i"),
matches("xÉ", "[^q]é", "i"),
matches(codepoints-to-string(8490), "[A-Z]", "i"),
matches("m", "\\p{Lu}", "i"),
matches("m", "\\P{Lu}", "i")
