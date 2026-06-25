(:JIQS: ShouldRun; Output="(true, true, false, false, false, false, false)" :)
matches("x[Y-z]", "X[y-Z]", "qi"),
matches("AbC", "abc", "i"),
matches("AbC", "abc"),
matches("O", "[A-Z-[OI]]", "i"),
matches("I", "[A-Z-[OI]]", "i"),
matches("q", "[^Q]", "i"),
matches("Q", "[^q]", "i")
